package org.example.enrichments;

import org.example.Message;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EnrichmentService {

  private final ConcurrentHashMap<EnrichmentType, Enrichment> enrichments =
      new ConcurrentHashMap<>();
  private final CopyOnWriteArrayList<keyAndValueHashMap> copyOnWriteArrayList =
      new CopyOnWriteArrayList<>();

  public void addNewEnrichment(EnrichmentType enrichmentType, Enrichment enrichment) {
    keyAndValueHashMap keyAndValueHashMap = new keyAndValueHashMap(enrichmentType, enrichment);
    this.copyOnWriteArrayList.add(keyAndValueHashMap);
  }

  public record keyAndValueHashMap(EnrichmentType enrichmentType, Enrichment enrichment) {}

  private void updateEnrichments() {
    for (keyAndValueHashMap keyAndValueHashMap : this.copyOnWriteArrayList) {
      EnrichmentType key = keyAndValueHashMap.enrichmentType;
      Enrichment value = keyAndValueHashMap.enrichment;
      this.enrichments.put(key, value);
    }
    this.copyOnWriteArrayList.clear();
  }

  public ArrayList<EnrichmentType> printEnrichmentType() {
    updateEnrichments();
    return new ArrayList<>(this.enrichments.keySet());
  }

  public Message enrich(Message message) {
    if (message == null) {
      return null;
    }
    Map<String, String> messageContent = message.getContent();
    EnrichmentType messageType = message.getEnrichmentType();

    updateEnrichments();

    Enrichment enrichment = this.enrichments.get(messageType);
    if (enrichment == null) {
      return message;
    }
    Map<String, String> newContent = enrichment.enrich(new ConcurrentHashMap<>(messageContent));
    message.setContent(newContent);
    return message;
  }
}
