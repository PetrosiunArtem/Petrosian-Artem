package org.example.service.exception;

public class CommentUpdateException extends RuntimeException {
  public CommentUpdateException(String message, Throwable cause) {
    super(message, cause);
  }
}
