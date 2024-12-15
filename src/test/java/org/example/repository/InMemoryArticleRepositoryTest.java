package org.example.repository;

import org.example.entity.Article;
import org.example.entity.ArticleId;
import org.example.repository.exception.ArticleNotFoundException;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.example.SqlQueries.CREATEARTICLE;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class InMemoryArticleRepositoryTest {
  @Container
  public static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:latest");

  private static Jdbi jdbi;

  @BeforeAll
  static void beforeAll() {
    String postgresJdbcUrl = POSTGRES.getJdbcUrl();
    Flyway flyway =
        Flyway.configure()
            .outOfOrder(true)
            .locations("classpath:db/migrations")
            .dataSource(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword())
            .load();
    flyway.migrate();
    jdbi = Jdbi.create(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword());
  }

  @BeforeEach
  void beforeEach() {
    jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM article").execute());
    jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM comment").execute());
  }

  @Test
  void generateId() {
    ArticleRepository articleRepository = new InMemoryArticleRepository(jdbi);
    ArticleId articleId = articleRepository.generateId();
    assertNotNull(articleId);
  }

  @Test
  void findAll() {
    ArticleRepository articleRepository = new InMemoryArticleRepository(jdbi);

    ArticleId articleId = articleRepository.generateId();
    assertNotNull(articleId);
    Set<String> testTags = new HashSet<>();
    testTags.add("tag1");
    testTags.add("tag2");

    Article article1 = new Article(articleId, "testTitle", testTags, null, false);
    articleRepository.create(article1);

    ArticleId newArticleId = articleRepository.generateId();
    assertNotNull(newArticleId);
    Set<String> newTestTags = new HashSet<>();
    newTestTags.add("new tag1");
    newTestTags.add("new tag2");

    Article article2 = new Article(newArticleId, "new testTitle", newTestTags, null, false);
    articleRepository.create(article2);

    List<Article> articles = articleRepository.findAll();
    assertNotNull(articles);
    assertEquals(2, articles.size());
    assertEquals(article1, articles.get(0));
    assertEquals(article2, articles.get(1));
  }

  @Test
  void findById() {

    ArticleRepository articleRepository = new InMemoryArticleRepository(jdbi);

    ArticleId articleId = articleRepository.generateId();
    assertNotNull(articleId);

    Set<String> testTags = new HashSet<>();
    testTags.add("drama");
    testTags.add("comedy");
    Article testArticle = new Article(articleId, "testTitle", testTags, null, false);
    ArticleId newArticleId = articleRepository.create(testArticle);
    jdbi.useTransaction(
        handle ->
            handle
                .createUpdate(CREATEARTICLE.getString())
                .bind("article_id", articleId.id())
                .bind("name", "My article")
                .bindArray("tags", String.class, testTags)
                .bindArray("comments_id", Long.class, new long[] {})
                .bind("trending", false)
                .execute());

    Article article = articleRepository.findById(newArticleId);
    assertEquals(testArticle, article);
  }

  @Test
  void create() {
    ArticleRepository articleRepository = new InMemoryArticleRepository(jdbi);

    ArticleId articleId = articleRepository.generateId();
    assertNotNull(articleId);

    Set<String> testTags = new HashSet<>();
    testTags.add("test_tag1");
    testTags.add("test_tag2");

    articleRepository.create(new Article(articleId, "testTitle", testTags, null, false));

    Article article = articleRepository.findById(articleId);

    assertEquals("testTitle", article.getName());
    assertEquals(testTags.toString(), article.getTags());
    assertEquals(0, article.getComments().size());
    assertFalse(article.getTrending());
  }

  @Test
  void update() {
    ArticleRepository articleRepository = new InMemoryArticleRepository(jdbi);

    ArticleId articleId = articleRepository.generateId();
    assertNotNull(articleId);

    Set<String> testTags = new HashSet<>();
    testTags.add("test_tag1");
    testTags.add("test_tag2");

    articleRepository.create(new Article(articleId, "testTitle", testTags, null, false));

    Set<String> newTags = new HashSet<>();
    newTags.add("new_test_tag1");
    newTags.add("new_test_tag2");

    Article newArticle =
        articleRepository.findById(articleId).withTags(newTags).withName("new_title");

    articleRepository.update(newArticle);

    Article updatedArticle = articleRepository.findById(articleId);

    assertEquals("new_title", updatedArticle.getName());
    assertEquals(newTags.toString(), updatedArticle.getTags());
    assertEquals(0, updatedArticle.getComments().size());
    assertFalse(updatedArticle.getTrending());
  }

  @Test
  void delete() {
    ArticleRepository articleRepository = new InMemoryArticleRepository(jdbi);

    ArticleId articleId = articleRepository.generateId();
    assertNotNull(articleId);
    Set<String> testTags = new HashSet<>();
    testTags.add("test_tag1");
    testTags.add("test_tag2");

    articleRepository.create(new Article(articleId, "testTitle", testTags, null, false));

    articleRepository.delete(articleId);

    assertThrows(ArticleNotFoundException.class, () -> articleRepository.findById(articleId));
  }
}
