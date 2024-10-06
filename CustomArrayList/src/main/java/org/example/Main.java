package org.example;

public class Main {
  public static void main(String[] args) {
    CustomArrayList<Integer> arr = new CustomArrayList<>();
    for (int i = 0; i < 31; i++) {
//      arr.add(String.valueOf(i));
      arr.add(i*2);
    }
    show(arr);
  }

  public static void show(CustomArrayList arr) {
    for (int i = 0; i < arr.size(); i++) {
      System.out.print(arr.get(i) + " ");
    }
  }
}
