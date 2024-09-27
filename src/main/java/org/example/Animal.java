package org.example;

import java.util.Random;

public class Animal {
  protected String name;
  protected String _animal;
  protected String _eat;
  protected String _diet;
  protected String _move;

  public Animal(String name, String animal, String eat, String diet, String move) {
    this.name = name;
    this._animal = animal;
    this._eat = eat;
    this._diet = diet;
    this._move = move;
  }

  public void hello() {
    System.out.println(this.name + " -- " + this._animal);
  }

  public void eat() {
    System.out.println(this.name + " ест " + this._eat);
  }

  public void move() {
    System.out.println(this.name + " " + this._move);
  }

  public void diet() {
    System.out.println(this.name + " -- " + this._diet);
  }

  public void action() {
    Random random = new Random();
    int randomNumber = random.nextInt(3);
    switch (randomNumber) {
      case 0:
        eat();
        break;
      case 1:
        move();
        break;
      case 2:
        diet();
        break;
    }
  }
}

