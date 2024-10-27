package org.example;

import org.example.enrichments.EnrichmentType;
import org.example.enrichments.MsisdnEnrichment;
import org.example.users.ConcurrencyHashMapUserRepository;
import org.example.users.User;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MsisdnEnrichmentTest {

  @Test
  public void enrich() {
    ConcurrencyHashMapUserRepository repository = new ConcurrencyHashMapUserRepository();
    User user = new User("Ваня", "Облепихович");
    String msisdn = "8800535535";
    repository.updateUserByMsisdn(msisdn, user);

    MsisdnEnrichment msisdnEnrichment = new MsisdnEnrichment(repository);
    Map<String, String> content =
        new HashMap<>(
            Map.of(
                "action", "button_click",
                "page", "book_card",
                "msisdn", msisdn));
    Message message = new Message(content, EnrichmentType.MSISDN);
    msisdnEnrichment.enrich(message.getContent());
    Map<String, String> currentResult = message.getContent();
    Map<String, String> expectedResult =
        new HashMap<>(
            Map.of(
                "action",
                "button_click",
                "page",
                "book_card",
                "msisdn",
                msisdn,
                "firstName",
                user.firstName(),
                "lastName",
                user.lastName()));
    assertEquals(currentResult, expectedResult);
  }
}
