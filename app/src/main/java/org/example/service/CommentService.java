package org.example.service;

import org.example.entity.ArticleId;
import org.example.entity.Comment;
import org.example.entity.CommentId;

import org.example.repository.CommentRepository;

import org.example.repository.exception.CommentIdDuplicatedException;
import org.example.repository.exception.CommentNotFoundException;

import org.example.service.exception.CommentCreateException;
import org.example.service.exception.CommentDeleteException;

public class CommentService {
  private final CommentRepository commentRepository;

  public CommentService(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  public CommentId create(ArticleId articleId, String text) {
    CommentId commentId = commentRepository.generateId();
    Comment comment = new Comment(commentId, articleId, text);
    try {
      commentRepository.create(comment);
    } catch (CommentIdDuplicatedException e) {
      throw new CommentCreateException("Cannot create comment", e);
    }
    return commentId;
  }

  public void delete(CommentId commentId) {
    try {
      commentRepository.delete(commentId);
    } catch (CommentNotFoundException e) {
      throw new CommentDeleteException("Cannot delete comment with id=" + commentId, e);
    }
  }
}
