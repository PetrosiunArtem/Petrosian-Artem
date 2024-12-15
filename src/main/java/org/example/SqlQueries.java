package org.example;

public enum SqlQueries {
  CREATEARTICLE(
      "INSERT INTO article (article_id, title, tags, comments, trending) VALUES"
          + " (:article_id, :title, :tags, :comments, :trending)"),

  CREATECOMMENT("INSERT INTO comment (comment_id, text) VALUES (:comment_id, :text)"),

  GENERATEARTICLEID("SELECT nextval('article_article_id_seq') AS value"),

  GENERATECOMMENTID("SELECT nextval('comment_comment_id_seq') AS value"),

  FINDALLARTICLES("SELECT * FROM article"),

  FINDARTICLE("SELECT * FROM article WHERE article_id = :article_id"),

  FINDCOMMENT("SELECT * FROM comment WHERE comment_id = :comment_id"),

  UPDATECOMENT("UPDATE comment SET text = :text WHERE comment_id = :comment_id"),

  UPDATEARTICLE(
      "UPDATE article SET title = :title, tags = :tags, comments = :comments, trending = :trending WHERE article_id = :article_id"),

  DELETECOMMENT("DELETE FROM comment WHERE comment_id = :comment_id"),

  DELETEARTICLE("DELETE FROM article WHERE article_id = :article_id");

  private final String query;

  private SqlQueries(String query) {
    this.query = query;
  }

  public String getString() {
    return query;
  }
}
