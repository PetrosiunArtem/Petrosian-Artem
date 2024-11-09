package org.example.entity;

import org.example.ArticleId;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Article {

  private final ArticleId id;
  private final String name;
  private final Set<String> tags;
  private final List<Comment> comments;

  public Article(ArticleId id, String name, Set<String> tags, List<Comment> comments) {
    this.id = id;
    this.name = name;
    this.tags = tags;
    this.comments = comments;
  }

  public Article withName(String newName) {
    return new Article(id, newName, tags, comments);
  }

  public Article withTags(Set<String> newTags) {
    return new Article(id, name, newTags, comments);
  }

  public Article withComments(List<Comment> newComments) {
    return new Article(id, name, tags, newComments);
  }

  public ArticleId getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Set<String> getTags() {
    return tags;
  }

  public List<Comment> getComments() {
    return comments;
  }

  @Override
  public String toString() {
    return "Article{" + "id=" + id + ", name='" + name + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Article article = (Article) o;
    return id.equals(article.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
