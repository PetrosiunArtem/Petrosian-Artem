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
import org.example.service.exception.CommentFindException;

import java.util.List;

public class CommentService {
  private final CommentRepository commentRepository;
  private final ArticleRepository articleRepository;

  public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository) {
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
      article = article.withComments(comments);
      if (!article.trending() && comments.size() > 3) {
        article = article.withTrending(true);
      }
      articleRepository.update(article);
    } catch (ArticleNotFoundException e) {
      throw new ArticleFindException("Cannot find article", e);
    }
    return commentId;
  }

  public void delete(CommentId commentId) {
    Comment comment;
    try {
      comment = commentRepository.findById(commentId);
    } catch (CommentNotFoundException e) {
      throw new CommentFindException("Cannot find comment", e);
    }

    try {
      Article article = articleRepository.findById(comment.articleId());
      List<Comment> comments = article.getComments();
      comments.remove(comment);
      article = article.withComments(comments);
      if (article.trending() && comments.size() <= 3) {
        article = article.withTrending(false);
      }
      articleRepository.update(article);
    } catch (ArticleNotFoundException e) {
      throw new ArticleFindException("Cannot find article", e);
    }

    try {
      commentRepository.delete(commentId);
    } catch (CommentNotFoundException e) {
      throw new CommentDeleteException("Cannot delete comment with id=" + commentId, e);
    }
  }
}
