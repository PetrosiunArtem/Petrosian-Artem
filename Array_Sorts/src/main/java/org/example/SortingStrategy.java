package org.example;

import java.util.List;

public interface SortingStrategy {
  List<Integer> sort(List<Integer> array);

  SortingType sortingType();
}
