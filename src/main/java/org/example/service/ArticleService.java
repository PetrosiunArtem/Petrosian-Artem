package org.example.service;

import org.example.TransactionManager;
import org.example.entity.Article;
import org.example.entity.ArticleId;
import org.example.entity.Comment;

import org.example.repository.ArticleRepository;
import org.example.repository.CommentRepository;
import org.example.repository.exception.ArticleIdDuplicatedException;
import org.example.repository.exception.ArticleNotFoundException;

import org.example.repository.exception.CommentNotFoundException;

import org.example.service.exception.ArticleCreateException;
import org.example.service.exception.ArticleDeleteException;
import org.example.service.exception.ArticleFindException;
import org.example.service.exception.ArticleUpdateException;
import org.example.service.exception.CommentDeleteException;

import org.jdbi.v3.core.transaction.TransactionIsolationLevel;

import java.util.List;
import java.util.Set;

public class ArticleService {
  private final ArticleRepository articleRepository;
  private final CommentRepository commentRepository;
  private final TransactionManager transactionManager;

  public ArticleService(
      ArticleRepository articleRepository,
      CommentRepository commentRepository,
      TransactionManager transactionManager) {
    this.articleRepository = articleRepository;
    this.commentRepository = commentRepository;
    this.transactionManager = transactionManager;
  }

  public ArticleId generateId() {
    return articleRepository.generateId();
  }

  public List<Article> findAll() {
    return articleRepository.findAll();
  }

  public Article findById(ArticleId articleId) {
    try {
      return articleRepository.findById(articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleFindException("Cannot find article by id=" + articleId, e);
    }
  }

  public ArticleId create(String name, Set<String> tags) {
    ArticleId articleId = articleRepository.generateId();
    Article article = new Article(articleId, name, tags, null, false);
    try {
      articleRepository.create(article);
    } catch (ArticleIdDuplicatedException e) {
      throw new ArticleCreateException("Cannot create article", e);
    }
    return articleId;
  }

  public void update(
      ArticleId articleId,
      String name,
      Set<String> tags,
      List<Comment> comments,
      boolean trending) {

    transactionManager.useTransaction(
        TransactionIsolationLevel.REPEATABLE_READ,
        () -> {
          Article article;
          try {
            article = articleRepository.findById(articleId);
          } catch (ArticleNotFoundException e) {
            throw new ArticleUpdateException("Cannot find article with id=" + articleId, e);
          }
          try {
            articleRepository.update(
                article
                    .withName(name)
                    .withTags(tags)
                    .withComments(comments)
                    .withTrending(trending));
          } catch (ArticleNotFoundException e) {
            throw new ArticleUpdateException(
                "Cannot update article with id = " + article.getId(), e);
          }
        });
  }

  public void delete(ArticleId articleId) {
    transactionManager.useTransaction(
        TransactionIsolationLevel.REPEATABLE_READ,
        () -> {
          Article article;
          try {
            article = articleRepository.findById(articleId);
          } catch (ArticleNotFoundException e) {
            throw new ArticleDeleteException("Cannot find article with id = " + articleId, e);
          }
          try {
            articleRepository.delete(articleId);
          } catch (ArticleNotFoundException e) {
            throw new ArticleDeleteException("Cannot delete article with id = " + articleId, e);
          }
          for (Comment comment : article.getComments()) {
            try {
              commentRepository.delete(comment.getId());
            } catch (CommentNotFoundException e) {
              throw new CommentDeleteException(
                  "Cannot delete comment with id = " + comment.getId(), e);
            }
          }
        });
  }

  public void createArticles(List<Article> articles) {
    transactionManager.useTransaction(
        TransactionIsolationLevel.READ_COMMITTED,
        () -> {
          for (Article article : articles) {
            try {
              articleRepository.create(article);
            } catch (ArticleIdDuplicatedException e) {
              throw new ArticleCreateException(
                  "Cannot create article with id = " + article.getId(), e);
            }
          }
        });
  }
}
