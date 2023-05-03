package ru.stqa.training.selenium.exercise19.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CustomerPanelLoginPage extends Page {

  public  CustomerPanelLoginPage (WebDriver driver) {
    super(driver);
  }

  public CustomerPanelLoginPage open(){
    driver.get("http://litecart.stqa.ru");
    return this;
  }

  public CustomerPanelLoginPage enterEmail (String email){
    driver.findElement(By.cssSelector("input[name='email']")).sendKeys(email);
    return this;
  }

  public CustomerPanelLoginPage enterPassword (String password) {
    driver.findElement(By.cssSelector("input[name='password']")).sendKeys(password);
    return this;
  }

  public void submitLogin (){
    driver.findElement(By.cssSelector("button[name='login']")).click();
  }


}
