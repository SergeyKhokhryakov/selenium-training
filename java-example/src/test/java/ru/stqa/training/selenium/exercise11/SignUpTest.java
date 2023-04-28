package ru.stqa.training.selenium.exercise11;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.stqa.training.selenium.TestBase;

import java.util.List;

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

    driver.findElement(By.cssSelector(".select2-selection__arrow")).click();
    driver.findElement(By.cssSelector(".select2-search__field")).sendKeys("Russi");
    List<WebElement> list = driver.findElements(By.cssSelector(".select2-results__options li"));
    if (list.size() != 0) {
      list.get(0).click();
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