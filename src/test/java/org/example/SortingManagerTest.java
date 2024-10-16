package org.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortingManagerTest {

  @Test
  void sort() {
    SortingManager manager = new SortingManager();
    manager.addSortingStrategy(new BubbleSortStrategy());
    final List<Integer> list = new ArrayList<>();
    list.add(-3);
    list.add(2121212);
    list.add(-5454);
    final ListCopyWrapper listCopy = new ListCopyWrapper(list);
    assertEquals(manager.sort(listCopy, SortingType.BUBBLE).getList(), List.of(-5454, -3, 2121212));
  }
}
