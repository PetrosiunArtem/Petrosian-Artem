package org.example;

public class QuickSortStrategy implements SortingStrategy {

  public static void quickSort(ListCopyWrapper list) {
    if (list.size() > (int) 1e7) {
      throw new RuntimeException(
          "Кол-во элементов list превысило 10^7, что время работы Quick sort будет не в рамках ТЗ");
    }
    quickSort(list, 0, list.size() - 1);
  }

  public static void quickSort(ListCopyWrapper list, int low, int high) {
    if (low >= high) {
      return;
    }
    int id = divide(list, low, high);
    quickSort(list, low, id - 1);
    quickSort(list, id, high);
  }

  public static int divide(ListCopyWrapper list, int low, int high) {
    int mid = low + (high - low) / 2;
    int pivot = list.get(mid);
    int left = low;
    int right = high;
    while (left <= right) {
      while (list.get(left) < pivot) {
        left++;
      }
      while (list.get(right) > pivot) {
        right--;
      }
      if (left > right) {
        continue;
      }

      int tmp = list.get(left);
      list.set(left, list.get(right));
      list.set(right, tmp);

      left++;
      right--;
    }
    return left;
  }

  @Override
  public ListCopyWrapper sort(ListCopyWrapper listCopy) {
    quickSort(listCopy);
    return listCopy;
  }

  @Override
  public SortingType sortingType() {
    return SortingType.QUICK;
  }
}
