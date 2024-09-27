package org.example;

class Dolphin extends Animal implements Predator, WaterFowl {
  public Dolphin(String name) {
    super(name, "Дельфин", eat, diet, move);
    this._eat = "Рыба";
  }
}

