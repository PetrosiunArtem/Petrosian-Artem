package org.example;

import org.example.enrichments.*;
import org.example.users.*;

import java.util.HashMap;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    ConcurrencyHashMapUserRepository repository = new ConcurrencyHashMapUserRepository();
    User user = new User("Ваня", "Облепихович");
    repository.updateUserByMsisdn("8800535535", user);

    MsisdnEnrichment msisdnEnrichment = new MsisdnEnrichment(repository);
    EnrichmentService service = new EnrichmentService();
    service.addNewEnrichment(EnrichmentType.MSISDN, msisdnEnrichment);

    Map<String, String> content =
        new HashMap<>(
            Map.of(
                "action", "button_click",
                "page", "book_card",
                "msisdn", "8800535535"));
    Message message = new Message(content, EnrichmentType.MSISDN);
    System.out.println(service.enrich(message).getContent());
    System.out.println(message.getContent());
  }
}
