package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BubbleSortStrategyTest {
  @Test
  void sort() {
    final BubbleSortStrategy strategy = new BubbleSortStrategy();
    final List<Integer> list = new ArrayList<>();
    list.add(453);
    list.add(-342);
    list.add(342);
    list.add(-342);
    list.add(1999);
    assertEquals(List.of(-342, -342, 342, 453, 1999), strategy.sort(list));
  }

  @Test
  void sortEmptyList() {
    final BubbleSortStrategy strategy = new BubbleSortStrategy();
    final List<Integer> list = new ArrayList<>();
    assertEquals(list, strategy.sort(list));
  }
}