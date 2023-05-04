package ru.stqa.training.selenium.exercise19.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MainPage extends Page {
  public  MainPage (WebDriver driver) {
    super(driver);
  }
  public void selectFirstProduct (){
    List<WebElement> elements = driver.findElements(By.cssSelector("#box-most-popular .products>li"));
    if (elements.size() > 0){
      elements.get(0).click();
    }
  }
}
