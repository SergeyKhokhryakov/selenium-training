package ru.stqa.training.selenium.exercise19.pages;

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
  String quantityXPath = "//div[@id='cart']//span[@class='quantity']";
  public By locatorCart(int quantity){
    return By.xpath(quantityXPath + "[.=" + quantity + "]");
  }
  public String quantityCart(){
    return driver.findElement(By.xpath(quantityXPath)).getText();
  }
  public String emptyCart(){
    return driver.findElement(locatorCart(0)).getText();
  }
}
