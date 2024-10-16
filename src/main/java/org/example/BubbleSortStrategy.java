package org.example;

public class BubbleSortStrategy implements SortingStrategy {

  @Override
  public ListCopyWrapper sort(ListCopyWrapper listCopy) {
    if (listCopy.size() > (int) 1e4) {
      throw new RuntimeException(
          "Кол-во элементов list превысило 10^4, что время работы Bubble sort будет не в рамках ТЗ");
    }

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
