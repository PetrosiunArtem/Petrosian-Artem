package org.example;

class Tiger extends Animal implements Predator, Terrestrial {
  public Tiger(String name) {
    super(name, "Тигр", eat, diet, move);
    this._eat = "Говядина";
  }
}

