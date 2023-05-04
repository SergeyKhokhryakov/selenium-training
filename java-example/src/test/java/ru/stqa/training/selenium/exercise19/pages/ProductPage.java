package ru.stqa.training.selenium.exercise19.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ProductPage extends Page {
  public  ProductPage (WebDriver driver) {
    super(driver);
  }
  public String nameProduct(){
    return driver.findElement(By.cssSelector("h1[class='title'")).getText();
  }
  public void selectSize(int size){
    new Select(driver.findElement(By.cssSelector("select[name='options[Size]']"))).selectByIndex(size);
  }
  public WebElement addToCartButton(){
    return driver.findElement(By.cssSelector("button[name='add_cart_product']"));
  }

}
