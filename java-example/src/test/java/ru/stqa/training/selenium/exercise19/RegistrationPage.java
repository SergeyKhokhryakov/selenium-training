package ru.stqa.training.selenium.exercise19;

import com.mifmif.common.regex.Generex;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

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
  public WebElement logoutLink (){
    return  driver.findElement(By.cssSelector(".content .list-vertical a[href*='logout']"));
  }
  public void selectCountry (String country){
    driver.findElement(By.cssSelector("[id ^= 'select2-country_code']")).click();
    driver.findElement(By.cssSelector(".select2-results__option[id $= '" + country + "']")).click();
  }
  public void selectZone (String zone){
    // если у страны имеются географические зоны (disabled == null), то ждем их подгрузки и выбираем требуемую зону
    // ииначе (disabled == "true") - пропускаем
    String disabled = driver.findElement(By.xpath("//select[@name='zone_code']")).getAttribute("disabled");
    if (disabled == null) {
      wait.until((WebDriver wd) -> wd.findElements(By.xpath("//select[@name='zone_code']/option")));
      new Select(driver.findElement(By.xpath("//select[@name='zone_code']"))).selectByValue(zone);
    }
  }
  public void inputPostCode (String postcode){
    // генерация строки postcode на основе регулярного выражения, соответствующего стране country
    // если для страны не определен шаблон regexp, то значением является аргумент (customer.getPostcode()) теста
    String postCode;
    String pattern = driver.findElement(By.cssSelector("input[name='postcode']")).getAttribute("pattern");
    if (pattern.length() == 0){
      postCode = postcode;
    } else {
      Generex generex = new Generex(pattern);
      postCode = generex.random();
    }
    driver.findElement(By.cssSelector("input[name='postcode']")).sendKeys(postCode);
  }

  public void inputPhone (String phone){
    String code = driver.findElement(By.cssSelector("input[name='phone']")).getAttribute("placeholder");
    driver.findElement(By.cssSelector("input[name='phone']")).sendKeys(code + phone);
  }

}
