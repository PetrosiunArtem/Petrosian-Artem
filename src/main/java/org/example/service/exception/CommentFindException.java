package org.example.service.exception;

public class CommentFindException extends RuntimeException {
  public CommentFindException(String message, Throwable cause) {
    super(message, cause);
  }
}
