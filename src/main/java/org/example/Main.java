package org.example;

public class Main {
  public static void main(String[] args) {
    Zoo zoo = new Zoo();
    zoo.check();
    zoo.addAnimal(new Horse("Дейзи"));
    zoo.check();
  }
}

