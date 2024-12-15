package org.example.service;

import org.example.entity.Article;
import org.example.entity.ArticleId;
import org.example.entity.Comment;

import java.util.List;
import java.util.Set;

public interface ServiceForArticle {
  ArticleId generateId();

  List<Article> findAll();

  Article findById(ArticleId articleId);

  ArticleId create(String name, Set<String> tags);

  void delete(ArticleId articleId);

  void createArticles(List<Article> articles);

  ArticleId update(
      ArticleId articleId, String name, Set<String> tags, List<Comment> comments, boolean trending);
}
