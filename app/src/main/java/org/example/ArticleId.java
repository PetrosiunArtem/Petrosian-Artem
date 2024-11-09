package org.example;

import java.util.Objects;

public class ArticleId {
  private final long id;

  public ArticleId(long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "ArticleId{" + "id=" + id + '\'' + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ArticleId articleId = (ArticleId) obj;
    return id == articleId.id;
  }

  public long getId() {
    return id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
