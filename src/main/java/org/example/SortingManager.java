package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class SortingManager {

  private final List<SortingStrategy> sorts = new ArrayList<>();

  public void addSortingStrategy(SortingStrategy sort) {
    this.sorts.add(sort);
  }

  public List<Integer> sort(List<Integer> list, SortingType type) {
    RuntimeException SortingTypeException = null;
    for (SortingStrategy strategy : this.sorts) {
      if (type != SortingType.ANY && strategy.sortingType() != type) {
        continue;
      }
      try {
        return strategy.sort(List.copyOf(list));
      } catch (RuntimeException exception) {
        SortingTypeException = exception;
      }
    }
    if (SortingTypeException != null) {
      throw SortingTypeException;
    }
    throw new NoSuchElementException("SortingStrategy with your sortingType does not exist");
  }
}
