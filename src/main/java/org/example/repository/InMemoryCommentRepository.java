package org.example.repository;

import org.example.entity.ArticleId;
import org.example.entity.CommentId;
import org.example.entity.Comment;

import org.example.repository.exception.CommentIdDuplicatedException;
import org.example.repository.exception.CommentNotFoundException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.jdbi.v3.core.statement.Update;

import static org.example.SqlQueries.FINDCOMMENT;
import static org.example.SqlQueries.DELETECOMMENT;
import static org.example.SqlQueries.UPDATECOMENT;
import static org.example.SqlQueries.CREATECOMMENT;
import static org.example.SqlQueries.GENERATECOMMENTID;

public class InMemoryCommentRepository implements CommentRepository {

  private final Jdbi jdbi;

  public InMemoryCommentRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  public CommentId generateId() {
    CommentId commentId;

    try (Handle handle = jdbi.open()) {
      commentId =
          new CommentId(
              (Long)
                  handle
                      .createQuery(GENERATECOMMENTID.getString())
                      .mapToMap()
                      .first()
                      .get("value"));
    }

    return commentId;
  }

  @Override
  public CommentId create(Comment comment) {
    CommentId commentId;
    try (Handle handle = jdbi.open()) {
      try (Update update = handle.createUpdate(CREATECOMMENT.getString())) {
        commentId =
            new CommentId(
                (long)
                    update
                        .bind("comment_id", comment.getId().id())
                        .bind("article_id", comment.articleId().id())
                        .bind("text", comment.getText())
                        .executeAndReturnGeneratedKeys("comment_id")
                        .mapToMap()
                        .first()
                        .get("comment_id"));
      } catch (UnableToExecuteStatementException e) {
        throw new CommentIdDuplicatedException(e.getMessage(), e);
      }
    }
    return commentId;
  }

  @Override
  public void delete(CommentId commentId) {
    try (Handle handle = jdbi.open()) {
      try (Update update = handle.createUpdate(DELETECOMMENT.getString())) {
        update.bind("comment_id", commentId.id()).execute();
      } catch (UnableToExecuteStatementException e) {
        throw new CommentNotFoundException(e.getMessage(), e);
      }
    }
  }

  @Override
  public Comment findById(CommentId commentId) {
    Comment comment;
    try (Handle handle = jdbi.open()) {
      ResultIterable<Comment> result =
          handle
              .createQuery(FINDCOMMENT.getString())
              .bind("comment_id", commentId.id())
              .map(
                  (rs, ctx) ->
                      new Comment(
                          new CommentId(rs.getLong("comment_id")),
                          new ArticleId(rs.getLong("article_id")),
                          rs.getString("text")));

      try {
        comment = result.first();
      } catch (IllegalStateException e) {
        throw new CommentNotFoundException(e.getMessage(), e);
      }
    }
    return comment;
  }

  @Override
  public void update(Comment comment) {
    try (Handle handle = jdbi.open()) {
      try (Update update = handle.createUpdate(UPDATECOMENT.getString())) {
        update
            .bind("comment_id", comment.getId())
            .bind("text", comment.getText())
            .executeAndReturnGeneratedKeys("comment_id")
            .mapToMap()
            .first()
            .get("comment_id");
      } catch (IllegalStateException e) {
        throw new CommentNotFoundException(e.getMessage(), e);
      }
    }
  }
}
