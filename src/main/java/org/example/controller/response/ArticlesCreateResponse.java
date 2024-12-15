package org.example.controller.response;

import org.example.entity.ArticleId;

import java.util.List;

public record ArticlesCreateResponse(List<ArticleId> articleIds) {}
