package org.example;

import java.util.Map;

public class MsisdnEnrichment implements Enrichment {
  private final UserRepository repository;

  MsisdnEnrichment(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public Map<String, String> enrich(Map<String, String> input) {
    String msisdn = input.get("msisdn");
    if (msisdn == null) {
      return input;
    }
    User user = this.repository.findByMsisdn(msisdn);
    if (user == null) {
      return input;
    }
    input.put("firstName", user.firstName());
    input.put("lastName", user.lastName());
    return input;
  }
}
