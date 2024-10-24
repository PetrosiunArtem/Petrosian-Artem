package org.example.enrichments;

import org.example.users.User;
import org.example.users.UserRepository;

import java.util.Map;

public class MsisdnEnrichment implements Enrichment {
  final String enrichType = EnrichmentType.MSISDN.toString().toLowerCase();
  private final UserRepository repository;

  public MsisdnEnrichment(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public Map<String, String> enrich(Map<String, String> input) {
    String msisdn = input.get(this.enrichType);
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
