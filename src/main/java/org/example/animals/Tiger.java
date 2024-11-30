package org.example.animals;

import org.example.characterization.Predator;
import org.example.characterization.Terrestrial;

public class Tiger extends Animal implements Predator, Terrestrial {
  public Tiger(String name) {
    super(name, "Тигр", eat, diet, move);
    setAnimalEat("Говядина");
  }
}
