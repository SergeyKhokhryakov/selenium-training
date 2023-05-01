package ru.stqa.training.selenium.exercise19;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignUpTest extends TestBase {

  //@EnabledOnOs(OS.MAC)

  @Test
  //@Order(2)
  public void signUpTest(){

    driver.get("http://litecart.stqa.ru");

    driver.findElement(By.cssSelector(".content a[href*='create_account']")).click();
    driver.findElement(By.cssSelector("input[name='firstname']")).sendKeys("Igor");
    driver.findElement(By.cssSelector("input[name='lastname']")).sendKeys("Prigozyn");
    driver.findElement(By.cssSelector("input[name='address1']")).sendKeys("street Tverskaya, 10-2-187");
    driver.findElement(By.cssSelector("input[name='address2']")).sendKeys("street Tverskaya, 1-1-110");
    driver.findElement(By.cssSelector("input[name='postcode']")).sendKeys("142125");
    driver.findElement(By.cssSelector("input[name='city']")).sendKeys("Moscow");

    driver.findElement(By.cssSelector("[id ^= 'select2-country_code']")).click();
    String countryCode = "RU";
    driver.findElement(By.cssSelector(".select2-results__option[id $= '" + countryCode + "']")).click();
    wait.until((WebDriver wd) -> wd.findElements(By.xpath("//select[@name='zone_code']/option")));
    String zoneCode = "GA";
    int length = driver.findElements(By.xpath("//select[@name='zone_code']/option")).size();
    if (length != 0){
      new Select(driver.findElement(By.xpath("//select[@name='zone_code']"))).selectByValue(zoneCode);
    }
    int index = (int) (Math.random() * 100);
    String email = "email_" + index + "@gmail.com";
    driver.findElement(By.cssSelector("input[name='email']")).sendKeys(email);
    String code = driver.findElement(By.cssSelector("input[name='phone']")).getAttribute("placeholder");
    driver.findElement(By.cssSelector("input[name='phone']")).sendKeys(code+"9111234419");
    String password = "12345";

    driver.findElement(By.cssSelector("input[name='password']")).sendKeys(password);
    driver.findElement(By.cssSelector("input[name='confirmed_password']")).sendKeys(password);
    driver.findElement(By.cssSelector("button[name='create_account']")).click();

    logout();

    loginUser(email, password);
    s.assertThat(driver.findElement(By.cssSelector("div [class='notice success']")).getText()).contains("logged in");
    logout();
  }

}