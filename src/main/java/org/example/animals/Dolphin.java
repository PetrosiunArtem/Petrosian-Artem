package org.example.animals;

import org.example.characterization.Predator;
import org.example.characterization.WaterFowl;

public class Dolphin extends Animal implements Predator, WaterFowl {
  public Dolphin(String name) {
    super(name, "Дельфин", eat, diet, move);
    setAnimalEat("Рыба");
  }
}
