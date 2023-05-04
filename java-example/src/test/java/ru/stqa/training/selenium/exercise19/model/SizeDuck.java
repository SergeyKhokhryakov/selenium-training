package ru.stqa.training.selenium.exercise19.model;

/**
 * Допустимые значения размеров товара "Желтая уточка" (Yellow Duck)
 *
 * @author Сергей Хохряков
 */
public enum SizeDuck {
  SMALL(1), MEDIUM(2), LARGE(3);
  private int value;

  SizeDuck(int i) {
    value = i;
  }

  public int value() {
    return value;
  }
}
