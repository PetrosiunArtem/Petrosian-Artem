package org.example.animals;

public class Animal {
  private String animalName;
  private String animalSpecies;
  private String animalEat;
  private String animalDiet;
  private String animalMove;

  public Animal(String name, String animal, String eat, String diet, String move) {
    this.animalName = name;
    this.animalSpecies = animal;
    this.animalEat = eat;
    this.animalDiet = diet;
    this.animalMove = move;
  }

  public String getAnimalName() {
    return this.animalName;
  }

  public String getAnimalSpecies() {
    return this.animalSpecies;
  }

  public String getAnimalEat() {
    return this.animalEat;
  }

  public String getAnimalDiet() {
    return this.animalDiet;
  }

  public String getAnimalMove() {
    return this.animalMove;
  }

  public void setAnimalEat(String eat) {
    this.animalEat = eat;
  }

  public void setAnimalName(String name) {
    this.animalName = name;
  }

  public void setAnimalSpecies(String species) {
    this.animalSpecies = species;
  }

  public void setAnimalDiet(String diet) {
    this.animalDiet = diet;
  }

  public void setAnimalMove(String move) {
    this.animalMove = move;
  }
}
