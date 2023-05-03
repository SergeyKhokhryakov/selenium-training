package ru.stqa.training.selenium.exercise19;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CommonPage extends Page {
  public  CommonPage (WebDriver driver) {
    super(driver);
  }
  public String textSuccess(){
    return driver.findElement(By.cssSelector("div [class='notice success']")).getText();
  }
  public WebElement homeLink (){
    return driver.findElement(By.cssSelector("#site-menu .general-0 a"));
  }
}
