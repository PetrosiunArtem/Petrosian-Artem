package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.example.controller.ArticleController;
import org.example.controller.ArticleFreemarkerController;
import org.example.controller.CommentController;
import org.example.manager.JdbiTransactionManager;
import org.example.manager.TransactionManager;
import org.example.repository.ArticleRepository;
import org.example.repository.CommentRepository;
import org.example.repository.InMemoryArticleRepository;
import org.example.repository.InMemoryCommentRepository;
import org.example.service.ArticleService;
import org.example.service.CommentService;
import org.example.service.RetryAbleArticleService;
import org.example.service.ServiceForArticle;
import org.example.template.TemplateFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;
import spark.Service;
import org.flywaydb.core.Flyway;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    Config config = ConfigFactory.load();

    Flyway flyway =
        Flyway.configure()
            .locations("classpath:db/migrations")
            .dataSource(
                config.getString("app.database.url"),
                config.getString("app.database.user"),
                config.getString("app.database.password"))
            .load();
    flyway.migrate();

    Jdbi jdbi =
        Jdbi.create(
            config.getString("app.database.url"),
            config.getString("app.database.user"),
            config.getString("app.database.password"));

    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();
    ArticleRepository articleRepository = new InMemoryArticleRepository(jdbi);
    CommentRepository commentRepository = new InMemoryCommentRepository(jdbi);
    TransactionManager transactionManager = new JdbiTransactionManager(jdbi);

    final CommentService commentService = new CommentService(commentRepository, articleRepository);

    ServiceForArticle articleService =
        new RetryAbleArticleService(
            new ArticleService(articleRepository, commentRepository, transactionManager),
            Retry.of(
                "retry-db",
                RetryConfig.custom()
                    .maxAttempts(3)
                    .retryExceptions(UnableToExecuteStatementException.class)
                    .build()));

    Application application =
        new Application(
            List.of(
                new ArticleController(service, articleService, objectMapper),
                new CommentController(service, commentService, objectMapper),
                new ArticleFreemarkerController(
                    service, articleService, TemplateFactory.freeMarkerEngine())));
    application.start();
  }
}
