package org.example;

class Eagle extends Animal implements Predator, Flyable {
  public Eagle(String name) {
    super(name, "Орёл", eat, diet, move);
  }
}

