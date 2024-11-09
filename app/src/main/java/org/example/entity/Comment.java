package org.example.entity;

import org.example.ArticleId;
import org.example.CommentId;

import java.util.Objects;

public class Comment {
  private final CommentId id;
  private final ArticleId articleId;
  private final String text;

  public Comment(CommentId id, ArticleId articleId, String text) {
    this.id = id;
    this.articleId = articleId;
    this.text = text;
  }

  public Comment withText(String newText) {
    return new Comment(id, articleId, newText);
  }

  public CommentId getId() {
    return id;
  }

  public ArticleId getArticleId() {
    return articleId;
  }

  public String getText() {
    return text;
  }

  @Override
  public String toString() {
    return "Comment{" + "CommentId=" + id + ", articleId='" + articleId + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return id.equals(comment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
