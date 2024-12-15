package org.example.service;

import io.github.resilience4j.retry.Retry;
import org.example.entity.Article;
import org.example.entity.ArticleId;
import org.example.entity.Comment;

import java.util.List;
import java.util.Set;

public class RetryAbleArticleService implements ServiceForArticle {
  private final ServiceForArticle delegate;
  private final Retry retry;

  public RetryAbleArticleService(ServiceForArticle articleService, Retry retry) {
    this.delegate = articleService;
    this.retry = retry;
  }

  public ArticleId generateId() {
    return delegate.generateId();
  }

  public List<Article> findAll() {
    return delegate.findAll();
  }

  public Article findById(ArticleId articleId) {
    return delegate.findById(articleId);
  }

  public ArticleId create(String name, Set<String> tags) {
    return delegate.create(name, tags);
  }

  public ArticleId update(
      ArticleId articleId,
      String name,
      Set<String> tags,
      List<Comment> comments,
      boolean trending) {
    return retry.executeSupplier(() -> delegate.update(articleId, name, tags, comments, trending));
  }

  public void delete(ArticleId articleId) {
    delegate.delete(articleId);
  }

  public void createArticles(List<Article> articles) {
    delegate.createArticles(articles);
  }
}
