package org.example.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record Article(
    ArticleId id, String name, Set<String> tags, List<Comment> comments, boolean trending) {

  public Article withName(String newName) {
    return new Article(id, newName, tags, comments, trending);
  }

  public Article withTags(Set<String> newTags) {
    return new Article(id, name, newTags, comments, trending);
  }

  public Article withComments(List<Comment> newComments) {
    return new Article(id, name, tags, newComments, trending);
  }

  public Article withTrending(boolean newTrending) {
    return new Article(id, name, tags, comments, newTrending);
  }

  public ArticleId getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getTags() {
    return tags.toString();
  }

  public boolean getTrending() {
    return trending;
  }

  public List<Comment> getComments() {
    return new ArrayList<>(comments);
  }
}
