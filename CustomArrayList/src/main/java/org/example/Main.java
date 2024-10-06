package org.example;

public class Main {
  public static void main(String[] args) {
    CustomArrayList<Integer> array = new CustomArrayList<>();
    for (int i = 0; i < 20; i++) {
      array.add(i);
    }
    array.remove(0);
    array.remove(0);
    array.remove(3);
    show(array);

    CustomArrayList<String> newArray = new CustomArrayList<>();
    for (int i = 0; i < 26; i++) {
      newArray.add(String.valueOf((char) ('a' + i)));
    }
    newArray.remove(1);
    newArray.remove(1);
    newArray.remove(2);
    show(newArray);
  }

  public static void show(CustomArrayList arr) {
    for (int i = 0; i < arr.size(); i++) {
      System.out.print(arr.get(i) + " ");
    }
    System.out.println();
  }
}
