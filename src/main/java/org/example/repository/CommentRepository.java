package org.example.repository;

import org.example.entity.CommentId;
import org.example.entity.Comment;

public interface CommentRepository {
  CommentId generateId();

  CommentId create(Comment comment);

  void delete(CommentId commentId);

  Comment findById(CommentId commentId);

  void update(Comment comment);
}
