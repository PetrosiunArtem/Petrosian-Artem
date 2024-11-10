package org.example.service;

import org.example.entity.Article;
import org.example.entity.ArticleId;
import org.example.entity.Comment;
import org.example.entity.CommentId;

import org.example.repository.ArticleRepository;
import org.example.repository.CommentRepository;

import org.example.repository.exception.ArticleNotFoundException;
import org.example.repository.exception.CommentIdDuplicatedException;
import org.example.repository.exception.CommentNotFoundException;

import org.example.service.exception.ArticleFindException;
import org.example.service.exception.CommentCreateException;
import org.example.service.exception.CommentDeleteException;

import java.util.List;

public class CommentService {
  private final CommentRepository commentRepository;
  private final ArticleRepository articleRepository;

  public CommentService(
      CommentRepository commentRepository,
      ArticleRepository articleRepository,
      ArticleService articleService) {
    this.commentRepository = commentRepository;
    this.articleRepository = articleRepository;
  }

  public CommentId create(ArticleId articleId, String text) {
    CommentId commentId = commentRepository.generateId();
    Comment comment = new Comment(commentId, articleId, text);
    try {
      commentRepository.create(comment);
    } catch (CommentIdDuplicatedException e) {
      throw new CommentCreateException("Cannot create comment", e);
    }
    try {
      Article article = articleRepository.findById(articleId);
      List<Comment> comments = article.getComments();
      comments.add(comment);
      article.withComments(comments);
      articleRepository.update(article);
    } catch (ArticleNotFoundException e) {
      throw new ArticleFindException("Cannot find article", e);
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
