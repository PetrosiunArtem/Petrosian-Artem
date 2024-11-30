package org.example;

import org.example.animals.*;

import java.util.ArrayList;

public class Zoo {
  private ArrayList<Animal> animals;

  public Zoo() {
    this.animals = new ArrayList<Animal>();
    Animal[] animals = {
      new Camel("Петя"),
      new Dolphin("Аквамен"),
      new Eagle("Джо зорький глаз"),
      new Horse("Платва"),
      new Tiger("Шрам"),
    };
    for (Animal animal : animals) {
      this.animals.add(animal);
    }
  }

  public ArrayList<Animal> getAnimals() {
    return this.animals;
  }

  public void addAnimal(Animal animal) {
    this.animals.add(animal);
  }
}
