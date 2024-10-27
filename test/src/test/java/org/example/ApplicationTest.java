package org.example;

import org.example.enrichments.EnrichmentService;
import org.example.enrichments.EnrichmentType;
import org.example.enrichments.MsisdnEnrichment;
import org.example.users.ConcurrencyHashMapUserRepository;
import org.example.users.User;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApplicationTest {
  private String createMsisdn() {
    long randomLongValue = (long) ((Math.random() + 1) * 1000000000L);
    return String.valueOf(randomLongValue);
  }

  private Message getMessageMsisdn(String msisdn) {
    Map<String, String> content = new HashMap<>(Map.of("msisdn", msisdn));
    return new Message(content, EnrichmentType.MSISDN);
  }

  @Test
  public void shouldSucceedEnrichmentInConcurrentEnvironmentSuccessfully()
      throws InterruptedException {
    EnrichmentService service = new EnrichmentService();
    int threadCount = 5;

    ConcurrencyHashMapUserRepository repository = new ConcurrencyHashMapUserRepository();
    List<String> msisdnElements = new CopyOnWriteArrayList<>();
    User user = new User("Иван", "Иван");

    for (int i = 0; i < threadCount; i++) {
      String msisdn = createMsisdn();
      msisdnElements.add(msisdn);
      repository.updateUserByMsisdn(msisdn, user);
    }
    MsisdnEnrichment msisdnEnrichment = new MsisdnEnrichment(repository);
    service.addNewEnrichment(EnrichmentType.MSISDN, msisdnEnrichment);

    List<Message> enrichmentResults = new CopyOnWriteArrayList<>();
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      int finalI = i;
      executorService.submit(
          () -> {
            enrichmentResults.add(service.enrich(getMessageMsisdn(msisdnElements.get(finalI))));
            latch.countDown();
          });
    }
    latch.await();

    List<Message> messageArrayList = new CopyOnWriteArrayList<>();

    String firstNameToString = "firstName";
    String lastNameToString = "lastName";
    String msisdnToString = "msisdn";

    for (int i = 0; i < threadCount; i++) {
      String msisdn = msisdnElements.get(i);
      Map<String, String> content =
          new ConcurrentHashMap<>() {
            {
              put(msisdnToString, msisdn);
              put(firstNameToString, user.firstName());
              put(lastNameToString, user.lastName());
            }
          };
      Message message = new Message(content, EnrichmentType.MSISDN);
      messageArrayList.add(message);
    }

    List<Map<String, String>> currentResult = new CopyOnWriteArrayList<>();
    List<Map<String, String>> extendedResult = new CopyOnWriteArrayList<>();

    for (Message message : enrichmentResults) {
      currentResult.add(message.getContent());
    }
    for (Message message : messageArrayList) {
      extendedResult.add(message.getContent());
    }

    assertTrue(
        extendedResult.size() == currentResult.size()
            && extendedResult.containsAll(currentResult)
            && currentResult.containsAll(extendedResult));
  }
}
