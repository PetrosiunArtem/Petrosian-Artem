package org.example.repository;

import org.example.CommentId;
import org.example.entity.Comment;

public interface CommentRepository {
  CommentId generateId();

  void create(Comment comment);

  void delete(CommentId commentId);
}
