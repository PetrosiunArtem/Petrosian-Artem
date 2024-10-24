package org.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnrichmentService {

  private final ConcurrentHashMap<EnrichmentType, Enrichment> enrichments =
      new ConcurrentHashMap<>();

  public void addNewEnrichment(EnrichmentType enrichmentType, Enrichment enrichment) {
    enrichments.put(enrichmentType, enrichment);
  }

  public Message enrich(Message message) {
    Enrichment enrichment = enrichments.get(message.enrichmentType());
    if (enrichment == null) {
      return message;
    }
    Map<String, String> newContent = enrichment.enrich(new ConcurrentHashMap<>(message.content()));
    Message newMessage = new Message(newContent, message.enrichmentType());
    return newMessage;
  }
}
