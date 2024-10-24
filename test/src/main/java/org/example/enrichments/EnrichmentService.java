package org.example.enrichments;

import org.example.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnrichmentService {

  private final ConcurrentHashMap<EnrichmentType, Enrichment> enrichments =
      new ConcurrentHashMap<>();

  public void addNewEnrichment(EnrichmentType enrichmentType, Enrichment enrichment) {
    enrichments.put(enrichmentType, enrichment);
  }

  public Message enrich(Message message) {
    if (message == null) {
      return message;
    }
    Map<String, String> messageContent = message.getContent();
    EnrichmentType messageType = message.getEnrichmentType();

    Enrichment enrichment = enrichments.get(messageType);
    if (enrichment == null) {
      return message;
    }
    Map<String, String> newContent = enrichment.enrich(new ConcurrentHashMap<>(messageContent));
    message.setContent(newContent);
    return message;
  }
}
