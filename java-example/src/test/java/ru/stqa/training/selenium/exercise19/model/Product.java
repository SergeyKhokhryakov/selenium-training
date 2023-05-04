package ru.stqa.training.selenium.exercise19.model;

/**
 * В текущей версии реализована только модель товара "Желтая уточка" (Yellow Duck)
 *
 * @author Сергей Хохряков
 */
public class Product {
  private String name;
  private SizeDuck size;

  public Product (){
    this.name = "Yellow Duck";
    this.size = SizeDuck.MEDIUM;
  }

  public String getName() {
    return name;
  }

  public void setSize(SizeDuck size) {
    this.size = size;
  }

  public SizeDuck size() {
    return size;
  }
}
