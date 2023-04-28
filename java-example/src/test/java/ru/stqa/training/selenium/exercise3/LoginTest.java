package ru.stqa.training.selenium.exercise3;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import ru.stqa.training.selenium.TestBase;

public class LoginTest extends TestBase {

  @Test
  public void loginTest() {
    driver.get("http://litecart.stqa.ru");
    loginUser("litecarts@gmail.com", "1234567890");
    s.assertThat(driver.findElement(By.cssSelector("div [class='notice success']")).getText()).contains("logged in");
  }

}