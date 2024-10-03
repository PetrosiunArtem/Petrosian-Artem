package org.example;

import org.example.animals.Animal;
import org.example.animals.Horse;

import java.util.Random;

public class Main {
  public static void main(String[] args) {
    Zoo zoo = new Zoo();
    check(zoo);
    zoo.addAnimal(new Horse("Дейзи"));
    check(zoo);
  }

  public static String action(Animal animal) {
    Random random = new Random();
    int randomNumber = random.nextInt(5);
    switch (randomNumber) {
      case 0:
        return animal.getAnimalSpecies();
      case 1:
        return animal.getAnimalDiet();
      case 2:
        return animal.getAnimalMove();
      case 3:
        return "ест " + animal.getAnimalEat();
    }
    return "ничего не делает";
  }

  public static void check(Zoo zoo) {
    for (Animal animal : zoo.getAnimals()) {
      System.out.println(animal.getAnimalName() + " " + animal.getAnimalSpecies());
      System.out.println(animal.getAnimalName() + " -- " + action(animal));
      System.out.println();
    }
  }
}
