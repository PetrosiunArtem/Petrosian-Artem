package org.example;

public interface SortingStrategy {
  ListCopyWrapper sort(ListCopyWrapper array);

  SortingType sortingType();
}
