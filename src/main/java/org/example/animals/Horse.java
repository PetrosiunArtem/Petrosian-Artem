package org.example.animals;

import org.example.characterization.Herbivores;
import org.example.characterization.Terrestrial;

public class Horse extends Animal implements Herbivores, Terrestrial {
  public Horse(String name) {
    super(name, "Лошадь", eat, diet, move);
  }
}
