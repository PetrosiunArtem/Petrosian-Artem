package org.example.animals;

import org.example.characterization.Herbivores;
import org.example.characterization.Terrestrial;

public class Camel extends Animal implements Herbivores, Terrestrial {
  public Camel(String name) {
    super(name, "Верблюд", eat, diet, move);
  }
}
