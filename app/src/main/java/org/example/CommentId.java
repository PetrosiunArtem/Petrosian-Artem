package org.example;

import java.util.Objects;

public class CommentId {
  private final long id;

  public CommentId(long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "CommentId{" + "id=" + id + '\'' + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    CommentId commentId = (CommentId) obj;
    return id == commentId.id;
  }

  public long getId() {
    return id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
