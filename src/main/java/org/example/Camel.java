package org.example;

class Camel extends Animal implements Herbivores, Terrestrial {
  public Camel(String name) {
    super(name, "Верблюд", eat, diet, move);
  }
}

