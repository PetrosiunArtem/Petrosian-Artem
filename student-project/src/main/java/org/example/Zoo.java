package org.example;

import java.util.ArrayList;
import java.util.Arrays;

class Zoo {
  ArrayList<Animal> animals;

  public Zoo() {
    this.animals = new ArrayList<Animal>();
    Animal[] animals = {
      new Camel("Петя"),
      new Dolphin("Аквамен"),
      new Eagle("Джо зорький глаз"),
      new Horse("Платва"),
      new Tiger("Шрам"),
    };
    this.animals.addAll(Arrays.asList(animals));
  }

  public void addAnimal(Animal animal) {
    this.animals.add(animal);
  }

  public void check() {
    for (Animal animal : this.animals) {
      animal.hello();
      animal.action();
      System.out.println('\n');
    }
  }
}

