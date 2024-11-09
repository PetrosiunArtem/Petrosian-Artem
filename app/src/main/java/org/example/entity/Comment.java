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
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Comment comment = (Comment) obj;
    return id.equals(comment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
