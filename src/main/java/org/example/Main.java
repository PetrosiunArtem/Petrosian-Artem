package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    final SortingManager manager = new SortingManager();

    manager.addSortingStrategy(new BubbleSortStrategy());
    manager.addSortingStrategy(new QuickSortStrategy());

    List<Integer> list = readList(scanner);
    SortingType type = choiceSortingTypes(scanner);
    ListCopyWrapper listCopy = new ListCopyWrapper(list);
    manager.sort(listCopy, type);
    System.out.println("Отсортированный list:");
    for (int i = 0; i < listCopy.size(); i++) {
      System.out.print(listCopy.get(i) + " ");
    }
  }

  public static List<Integer> readList(Scanner input) {
    System.out.println("Введите длину list, затем сам list");
    int n = input.nextInt();
    ArrayList<Integer> array = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      int value = input.nextInt();
      array.add(value);
    }
    return array;
  }

  public static void printSortingTypes() {
    System.out.println("Доступны следующие сортировки:");
    for (SortingType type : SortingType.values()) {
      System.out.println(type);
    }
  }

  public static SortingType choiceSortingTypes(Scanner scanner) {
    printSortingTypes();
    System.out.println("Выберете один из этих типов сортировок");
    String type = scanner.next().toUpperCase();
    SortingType sortingType = SortingType.ANY;
    try {
      sortingType = SortingType.valueOf(type);
    } catch (IllegalArgumentException exception) {
      System.out.println("Неизвестный тип сортировки: " + type + "\n Выбран тип по умолчанию");
    }
    return sortingType;
  }
}
