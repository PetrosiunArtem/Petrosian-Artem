package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.example.Application;

import org.example.manager.JdbiTransactionManager;
import org.example.repository.InMemoryArticleRepository;
import org.example.repository.InMemoryCommentRepository;
import org.example.service.ArticleService;
import org.example.service.CommentService;
import org.example.service.RetryAbleArticleService;
import org.example.service.ServiceForArticle;
import org.example.template.TemplateFactory;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import spark.Service;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Testcontainers
class ArticleControllerTest {

  @Container
  public static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:latest");

  private static Jdbi jdbi;
  private static Service service;

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
    service = Service.ignite();
  }

  @BeforeEach
  void beforeEach() {
    jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM article").execute());
    jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM comment").execute());
    ObjectMapper objectMapper = new ObjectMapper();

    ServiceForArticle articleService =
        new RetryAbleArticleService(
            new ArticleService(
                new InMemoryArticleRepository(jdbi),
                new InMemoryCommentRepository(jdbi),
                new JdbiTransactionManager(jdbi)),
            Retry.of(
                "retry-db",
                RetryConfig.custom()
                    .maxAttempts(3)
                    .retryExceptions(UnableToExecuteStatementException.class)
                    .build()));

    CommentService commentService =
        new CommentService(
            new InMemoryCommentRepository(jdbi), new InMemoryArticleRepository(jdbi));

    Application application =
        new Application(
            List.of(
                new ArticleController(service, articleService, objectMapper),
                new CommentController(service, commentService, objectMapper),
                new ArticleFreemarkerController(
                    service, articleService, TemplateFactory.freeMarkerEngine())));
    application.start();
    service.awaitInitialization();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void should201IfArticleIsSuccessfullyCreated() throws Exception {
    HttpResponse<String> response =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .POST(
                        HttpRequest.BodyPublishers.ofString(
                            """
                                  {"name": "test", "tags": ["drama"]}"""))
                    .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(201, response.statusCode());
  }

  @Test
  void should200IfArticleIsSuccessfullyUpdated() throws Exception {
    HttpResponse<String> response =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .POST(
                        HttpRequest.BodyPublishers.ofString(
                            """
                                  {"name": "test", "tags": ["drama"]}"""))
                    .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(201, response.statusCode());

    HttpResponse<String> responseSecond =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .PUT(
                        HttpRequest.BodyPublishers.ofString(
                            """
                                    {"name": "No testing", "tags": ["sci-fi"]}"""))
                    .uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(200, responseSecond.statusCode());
  }

  @Test
  void should404IfArticleDoesNotUpdated() throws Exception {
    HttpResponse<String> response =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .PUT(
                        HttpRequest.BodyPublishers.ofString(
                            """
                                    {"name": "No testing", "tags": ["sci-fi"]}"""))
                    .uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(404, response.statusCode());
  }

  @Test
  void should200IfArticleIsSuccessfullyDeleted() throws Exception {
    HttpResponse<String> response =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .POST(
                        HttpRequest.BodyPublishers.ofString(
                            """
                                  {"name": "test", "tags": ["drama"]}"""))
                    .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(201, response.statusCode());

    HttpResponse<String> responseSecond =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .DELETE()
                    .uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(200, responseSecond.statusCode());
  }

  @Test
  void should404IfArticleDoesNotDeleted() throws Exception {
    HttpResponse<String> responseSecond =
        HttpClient.newHttpClient()
            .send(
                HttpRequest.newBuilder()
                    .DELETE()
                    .uri(URI.create("http://localhost:%d/api/articles/1".formatted(service.port())))
                    .build(),
                HttpResponse.BodyHandlers.ofString(UTF_8));

    assertEquals(404, responseSecond.statusCode());
  }
}
