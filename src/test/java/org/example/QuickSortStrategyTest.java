package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuickSortStrategyTest {

  @Test
  public void sort() {
    final QuickSortStrategy strategy = new QuickSortStrategy();
    final List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(-2);
    list.add(3);
    list.add(-4);
    list.add(5);
    final ListCopyWrapper listCopy = new ListCopyWrapper(list);
    assertEquals(List.of(-4, -2, 1, 3, 5), strategy.sort(listCopy).getList());
  }

  @Test
  public void sortingType() {
    final QuickSortStrategy strategy = new QuickSortStrategy();
    assertEquals(strategy.sortingType(), SortingType.QUICK);
  }
}
