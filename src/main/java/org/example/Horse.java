package org.example;

class Horse extends Animal implements Herbivores, Terrestrial {
  public Horse(String name) {
    super(name, "Лошадь", eat, diet, move);
  }
}

