package org.example.animals;

import org.example.characterization.Flyable;
import org.example.characterization.Predator;

public class Eagle extends Animal implements Predator, Flyable {
  public Eagle(String name) {
    super(name, "Орёл", eat, diet, move);
  }
}
