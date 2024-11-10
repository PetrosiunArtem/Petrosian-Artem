package org.example;

import org.example.enrichments.EnrichmentService;
import org.example.enrichments.EnrichmentType;
import org.example.enrichments.MsisdnEnrichment;
import org.example.users.ConcurrencyHashMapUserRepository;
import org.example.users.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EnrichmentServiceTest {

  @Test
  public void addNewEnrichment() {
    ConcurrencyHashMapUserRepository repository = new ConcurrencyHashMapUserRepository();
    User user = new User("Ваня", "Облепихович");
    String msisdn = "8800535535";
    repository.updateUserByMsisdn(msisdn, user);

    MsisdnEnrichment msisdnEnrichment = new MsisdnEnrichment(repository);
    EnrichmentService service = new EnrichmentService();
    service.addNewEnrichment(EnrichmentType.MSISDN, msisdnEnrichment);
    ArrayList<EnrichmentType> currentResult = service.printEnrichmentType();
    ArrayList<EnrichmentType> expectedResult =
        new ArrayList<>() {
          {
            add(EnrichmentType.MSISDN);
          }
        };
    assertEquals(currentResult, expectedResult);
  }

  @Test
  public void enrichWithoutUser() {
    ConcurrencyHashMapUserRepository repository = new ConcurrencyHashMapUserRepository();
    MsisdnEnrichment msisdnEnrichment = new MsisdnEnrichment(repository);
    EnrichmentService service = new EnrichmentService();
    service.addNewEnrichment(EnrichmentType.MSISDN, msisdnEnrichment);

    Map<String, String> content =
        new HashMap<>(
            Map.of(
                "action", "button_click",
                "page", "book_card",
                "time", "10",
                "msisdn", "8800535535"));
    Message message = new Message(content, EnrichmentType.MSISDN);
    Map<String, String> currentRes = service.enrich(message).getContent();
    Map<String, String> expectedResult = content;
    assertEquals(currentRes, expectedResult);
  }
}
