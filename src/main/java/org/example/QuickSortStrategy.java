package org.example;

import java.util.ArrayList;
import java.util.List;

public class QuickSortStrategy implements SortingStrategy {

  public static void quickSort(List<Integer> list) {
    quickSort(list, 0, list.size() - 1);
  }

  public static void quickSort(List<Integer> list, int low, int high) {
    if (low >= high) {
      return;
    }
    int id = divide(list, low, high);
    quickSort(list, low, id - 1);
    quickSort(list, id, high);
  }

  public static int divide(List<Integer> list, int low, int high) {
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
  public List<Integer> sort(List<Integer> list) {
    List<Integer> listCopy = new ArrayList<>(list);
    quickSort(listCopy);
    return listCopy;
  }

  @Override
  public SortingType sortingType() {
    return SortingType.QUICK;
  }
}
