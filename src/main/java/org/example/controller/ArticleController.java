package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.controller.request.ArticleCreateRequest;
import org.example.controller.request.ArticleDeleteRequest;
import org.example.controller.request.ArticleFindAllRequest;
import org.example.controller.request.ArticleUpdateRequest;
import org.example.controller.request.ArticleFindByIdRequest;

import org.example.controller.response.ArticleUpdateResponse;
import org.example.controller.response.ArticlesCreateResponse;
import org.example.controller.response.ErrorResponse;
import org.example.controller.response.ArticleDeleteResponse;
import org.example.controller.response.ArticleFindByIdResponse;

import org.example.entity.Article;
import org.example.entity.ArticleId;
import org.example.service.ServiceForArticle;
import org.example.service.exception.ArticleCreateException;

import org.example.service.exception.ArticleDeleteException;
import org.example.service.exception.ArticleFindException;
import org.example.service.exception.ArticleUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Service;

import java.util.*;

public class ArticleController implements Controller {

  private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

  private final Service service;
  private final ServiceForArticle articleService;
  private final ObjectMapper objectMapper;

  public ArticleController(
      Service service, ServiceForArticle articleService, ObjectMapper objectMapper) {
    this.service = service;
    this.articleService = articleService;
    this.objectMapper = objectMapper;
  }

  @Override
  public void initializeEndpoints() {
    createArticle();
    createArticles();
    getArticle();
    updateArticle();
    deleteArticle();
    findAllArticle();
  }

  private void createArticle() {
    service.post(
        "/api/articles",
        (Request request, Response response) -> {
          response.type("application/json");
          String body = request.body();
          ArticleCreateRequest articleCreateRequest =
              objectMapper.readValue(body, ArticleCreateRequest.class);
          try {
            ArticleId articleId =
                articleService.create(articleCreateRequest.name(), articleCreateRequest.tags());
            LOG.debug("Article created");
            response.status(201);
            return objectMapper.writeValueAsString(articleId);
          } catch (ArticleCreateException e) {
            LOG.warn("Cannot create article", e);
            response.status(400);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }

  private void getArticle() {
    service.get(
        "/api/articles/:articleId",
        (Request request, Response response) -> {
          response.type("application/json");
          ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
          ArticleFindByIdRequest articleFindByIdRequest = new ArticleFindByIdRequest(articleId);
          try {
            Article article = articleService.findById(articleFindByIdRequest.articleId());
            LOG.debug("Article found");
            response.status(200);
            return objectMapper.writeValueAsString(new ArticleFindByIdResponse(article));
          } catch (ArticleFindException e) {
            LOG.warn("Cannot find article", e);
            response.status(404);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }

  private void updateArticle() {
    service.put(
        "/api/articles/:articleId",
        (Request request, Response response) -> {
          response.type("application/json");
          String body = request.body();
          ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
          ArticleUpdateRequest articleUpdateRequest =
              objectMapper.readValue(body, ArticleUpdateRequest.class);
          try {
            articleService.update(
                articleId,
                articleUpdateRequest.name(),
                articleUpdateRequest.tags(),
                articleUpdateRequest.comments(),
                articleUpdateRequest.trending());
            LOG.debug("Article updated");
            response.status(200);
            return objectMapper.writeValueAsString(new ArticleUpdateResponse());
          } catch (ArticleUpdateException e) {
            LOG.warn("Cannot find article", e);
            response.status(404);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }

  private void deleteArticle() {
    service.delete(
        "/api/articles/:articleId",
        (Request request, Response response) -> {
          response.type("application/json");
          ArticleId articleId = new ArticleId(Long.parseLong(request.params("articleId")));
          ArticleDeleteRequest articleDeleteRequest = new ArticleDeleteRequest(articleId);
          try {
            articleService.delete(articleDeleteRequest.articleId());
            response.status(200);
            LOG.debug("Article deleted");
            return objectMapper.writeValueAsString(new ArticleDeleteResponse());
          } catch (ArticleDeleteException e) {
            LOG.warn("Cannot deleted article", e);
            response.status(404);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }

  private void findAllArticle() {
    service.get(
        "/api/articles",
        (Request request, Response response) -> {
          response.type("application/json");
          String body = request.body();
          ArticleFindAllRequest articleFindAllRequest =
              objectMapper.readValue(body, ArticleFindAllRequest.class);
          try {
            List<Article> articles = articleService.findAll();
            List<Map<String, String>> articleMapList =
                articles.stream()
                    .map(
                        article ->
                            Map.of(
                                "name",
                                article.getName(),
                                "tags",
                                article.getTags(),
                                "comments",
                                article.getComments().toString()))
                    .toList();
            Map<String, Object> model = new HashMap<>();
            model.put("articles", articleMapList);
            LOG.debug("Articles showed");
            response.status(200);
            return objectMapper.writeValueAsString(model);
          } catch (ArticleFindException e) {
            LOG.warn("Cannot find article", e);
            response.status(404);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }

  private void createArticles() {
    service.post(
        "api/articles",
        (Request request, Response response) -> {
          response.type("application/json");
          String body = request.body();
          List<ArticleCreateRequest> articlesRequest;

          try {
            articlesRequest =
                Arrays.asList(objectMapper.readValue(body, ArticleCreateRequest[].class));
          } catch (JsonProcessingException e) {
            LOG.warn(e.getMessage());
            response.status(400);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }

          List<Article> articles = new ArrayList<>();

          for (ArticleCreateRequest article : articlesRequest) {
            articles.add(
                new Article(
                    articleService.generateId(), article.name(), article.tags(), null, false));
          }
          articleService.createArticles(articles);
          LOG.debug("Successfully created articles");
          response.status(200);
          return objectMapper.writeValueAsString(
              new ArticlesCreateResponse(articles.stream().map(Article::getId).toList()));
        });
  }
}
