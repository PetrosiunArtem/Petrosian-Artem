package org.example;

import org.example.enrichments.EnrichmentType;

import java.util.Map;

public class Message {
  private Map<String, String> content;
  private EnrichmentType enrichmentType;

  public Message(Map<String, String> content, EnrichmentType enrichmentType) {
    this.content = content;
    this.enrichmentType = enrichmentType;
  }

  public Map<String, String> getContent() {
    return this.content;
  }

  public EnrichmentType getEnrichmentType() {
    return this.enrichmentType;
  }

  public void setContent(Map<String, String> content) {
    this.content = content;
  }
}
