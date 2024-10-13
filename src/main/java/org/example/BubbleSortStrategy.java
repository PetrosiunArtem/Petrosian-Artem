package org.example;

import java.util.ArrayList;
import java.util.List;

public class BubbleSortStrategy implements SortingStrategy {

  @Override
  public List<Integer> sort(List<Integer> list) {
    List<Integer> listCopy = new ArrayList<>(list);
    for (int i = 0; i < listCopy.size(); i++) {
      for (int j = i + 1; j < listCopy.size(); j++) {
        if (listCopy.get(i) > listCopy.get(j)) {
          int temp = listCopy.get(i);
          listCopy.set(i, listCopy.get(j));
          listCopy.set(j, temp);
        }
      }
    }
    return listCopy;
  }

  @Override
  public SortingType sortingType() {
    return SortingType.BUBBLE;
  }
}
