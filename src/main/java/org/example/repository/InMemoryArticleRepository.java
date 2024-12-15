package org.example.repository;

import org.example.entity.ArticleId;
import org.example.entity.Article;

import org.example.entity.Comment;
import org.example.repository.exception.ArticleIdDuplicatedException;
import org.example.repository.exception.ArticleNotFoundException;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jdbi.v3.core.statement.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.SqlQueries.GENERATEARTICLEID;
import static org.example.SqlQueries.FINDALLARTICLES;
import static org.example.SqlQueries.FINDARTICLE;
import static org.example.SqlQueries.CREATEARTICLE;
import static org.example.SqlQueries.UPDATEARTICLE;
import static org.example.SqlQueries.DELETEARTICLE;

public class InMemoryArticleRepository implements ArticleRepository {

  private final Jdbi jdbi;

  public InMemoryArticleRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  public ArticleId generateId() {
    ArticleId id;
    try (Handle handle = jdbi.open()) {
      id =
          new ArticleId(
              (long)
                  handle
                      .createQuery(GENERATEARTICLEID.getString())
                      .mapToMap()
                      .first()
                      .get("value"));
    }
    return id;
  }

  @Override
  public List<Article> findAll() {
    List<Article> articles;
    try (Handle handle = jdbi.open()) {
      articles =
          handle
              .createQuery(FINDALLARTICLES.getString())
              .map(
                  (rs, ctx) ->
                      new Article(
                          new ArticleId(rs.getLong("article_id")),
                          rs.getString("name"),
                          Stream.of((String[]) rs.getArray("tags").getArray())
                              .collect(Collectors.toSet()),
                          rs.getArray("comments") == null
                              ? new ArrayList<>()
                              : Stream.of((Comment[]) rs.getArray("comments").getArray())
                                  .collect(Collectors.toList()),
                          rs.getBoolean("trending")))
              .list();
    }

    return articles;
  }

  @Override
  public Article findById(ArticleId articleId) {
    Article article;
    try (Handle handle = jdbi.open()) {
      ResultIterable<Article> result =
          handle
              .createQuery(FINDARTICLE.getString())
              .bind("article_id", articleId.id())
              .map(
                  (rs, ctx) ->
                      new Article(
                          new ArticleId(rs.getLong("article_id")),
                          rs.getString("name"),
                          Stream.of((String[]) rs.getArray("tags").getArray())
                              .collect(Collectors.toSet()),
                          rs.getArray("comments") == null
                              ? new ArrayList<>()
                              : Stream.of((Comment[]) rs.getArray("comments").getArray())
                                  .collect(Collectors.toList()),
                          rs.getBoolean("trending")));

      try {
        article = result.first();
      } catch (IllegalStateException e) {
        throw new ArticleNotFoundException(e.getMessage(), e);
      }
    }
    return article;
  }

  @Override
  public ArticleId create(Article article) {
    ArticleId articleId;
    try (Handle handle = jdbi.open()) {
      try (Update update = handle.createUpdate(CREATEARTICLE.getString())) {
        articleId =
            new ArticleId(
                (long)
                    update
                        .bind("article_id", article.getId().id())
                        .bind("name", article.getName())
                        .bindArray("tags", String.class, article.getTags())
                        .bindArray("comments", Comment.class, article.getComments())
                        .bind("trending", article.getTrending())
                        .executeAndReturnGeneratedKeys("article_id")
                        .mapToMap()
                        .first()
                        .get("article_id"));
      } catch (UnableToExecuteStatementException e) {
        throw new ArticleIdDuplicatedException(e.getMessage(), e);
      }
    }
    return articleId;
  }

  @Override
  public synchronized void update(Article article) {
    try (Handle handle = jdbi.open()) {
      try (Update update = handle.createUpdate(UPDATEARTICLE.getString())) {
        update
            .bind("article_id", article.getId().id())
            .bind("name", article.getName())
            .bindArray("tags", String.class, article.getTags())
            .bindArray("comments_id", Long.class, article.getComments())
            .bind("trending", article.getTrending())
            .executeAndReturnGeneratedKeys("article_id")
            .mapToMap()
            .first()
            .get("article_id");
      } catch (IllegalStateException e) {
        throw new ArticleNotFoundException(e.getMessage(), e);
      }
    }
  }

  @Override
  public void delete(ArticleId articleId) {
    try (Handle handle = jdbi.open()) {
      try (Update update = handle.createUpdate(DELETEARTICLE.getString())) {
        update
            .bind("article_id", articleId.id())
            .executeAndReturnGeneratedKeys("article_id")
            .mapToMap()
            .first()
            .get("article_id");
      } catch (IllegalStateException e) {
        throw new ArticleNotFoundException(e.getMessage(), e);
      }
    }
  }
}
