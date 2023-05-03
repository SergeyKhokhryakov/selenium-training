package ru.stqa.training.selenium.exercise19;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RegistrationPage extends Page {
  public RegistrationPage (WebDriver driver){
    super(driver);
  }
  public void open(){
    driver.get("http://litecart.stqa.ru");
    driver.findElement(By.cssSelector(".content a[href*='create_account']")).click();
  }
  public WebElement firstnameInput(){
    return driver.findElement(By.cssSelector("input[name='firstname']"));
  }
  public WebElement lastnameInput(){
    return driver.findElement(By.cssSelector("input[name='lastname']"));
  }
  public WebElement address1Input(){
    return driver.findElement(By.cssSelector("input[name='address1']"));
  }
  public WebElement cityInput(){
    return driver.findElement(By.cssSelector("input[name='city']"));
  }
  public WebElement emailInput (){
    return driver.findElement(By.cssSelector("input[name='email']"));
  }
  public WebElement phoneInput(){
    return driver.findElement(By.cssSelector("input[name='phone']"));
  }
  public WebElement passwordInput(){
    return driver.findElement(By.cssSelector("input[name='password']"));
  }
  public WebElement confirmedPasswordInput(){
    return driver.findElement(By.cssSelector("input[name='confirmed_password']"));
  }
  public WebElement createAccountButton (){
    return driver.findElement(By.cssSelector("button[name='create_account']"));
  }
}
