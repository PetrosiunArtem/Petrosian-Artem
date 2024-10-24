package org.example;

import java.util.HashMap;
import java.util.Map;

public interface Enrichment {

  Map<String, String> enrich(Map<String, String> input);
}
