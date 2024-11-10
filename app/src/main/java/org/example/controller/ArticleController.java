package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.controller.request.*;
import org.example.controller.response.*;

import org.example.entity.Article;
import org.example.entity.ArticleId;
import org.example.repository.exception.ArticleNotFoundException;
import org.example.service.ArticleService;
import org.example.service.exception.ArticleCreateException;

import org.example.service.exception.ArticleFindException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Service;

public class ArticleController implements Controller {

  private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

  private final Service service;
  private final ArticleService articleService;
  private final ObjectMapper objectMapper;

  public ArticleController(
      Service service, ArticleService articleService, ObjectMapper objectMapper) {
    this.service = service;
    this.articleService = articleService;
    this.objectMapper = objectMapper;
  }

  @Override
  public void initializeEndpoints() {
    createArticle();
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
                articleService.create(
                    articleCreateRequest.name(),
                    articleCreateRequest.tags(),
                    articleCreateRequest.comments());
            response.status(201);
            return objectMapper.writeValueAsString(new ArticleCreateResponse(articleId));
          } catch (ArticleCreateException e) {
            LOG.warn("Cannot create article", e);
            response.status(400);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }

  private void getArticle() {
    service.get(
        "/api/articles",
        (Request request, Response response) -> {
          response.type("application/json");
          String body = request.body();
          ArticleFindByIdRequest articleFindByIdRequest =
              objectMapper.readValue(body, ArticleFindByIdRequest.class);
          try {
            Article article = articleService.findById(articleFindByIdRequest.articleId());
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
        "/api/articles",
        (Request request, Response response) -> {
          response.type("application/json");
          String body = request.body();
          ArticleUpdateRequest articleUpdateRequest =
              objectMapper.readValue(body, ArticleUpdateRequest.class);
          try {
            articleService.update(
                articleUpdateRequest.articleId(),
                articleUpdateRequest.name(),
                articleUpdateRequest.tags(),
                articleUpdateRequest.comments());
            response.status(200);
            return objectMapper.writeValueAsString(new ArticleUpdateResponse());
          } catch (ArticleFindException e) {
            LOG.warn("Cannot find article", e);
            response.status(404);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }

  private void deleteArticle() {
    service.delete(
        "/api/articles",
        (Request request, Response response) -> {
          response.type("application/json");
          String body = request.body();
          ArticleDeleteRequest articleDeleteRequest =
              objectMapper.readValue(body, ArticleDeleteRequest.class);
          try {
            articleService.delete(articleDeleteRequest.articleId());
            response.status(200);
            return objectMapper.writeValueAsString(new ArticleDeleteResponse());
          } catch (ArticleFindException e) {
            LOG.warn("Cannot find article", e);
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
            articleService.findAll();
            response.status(200);
            return objectMapper.writeValueAsString(new ArticleFindAllResponse());
          } catch (ArticleFindException e) {
            LOG.warn("Cannot find article", e);
            response.status(404);
            return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
          }
        });
  }
}
