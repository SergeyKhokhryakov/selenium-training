package ru.stqa.training.selenium.exercise3;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginTest {
  public static WebDriver driver;
  //public static SafariDriver driver;
  public static WebDriverWait wait;
  //@EnabledOnOs(OS.MAC)
  @BeforeAll
  static public void start() {
      driver = new EdgeDriver();
      //SafariOptions options = new SafariOptions();
      //driver = new SafariDriver(options);
      wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
  @Test
  public void loginTest() {
    driver.get("http://litecart.stqa.ru");
    driver.findElement(By.cssSelector("input[name='email']")).sendKeys("litecart@gmail.com");
    driver.findElement(By.cssSelector("input[name='password']")).sendKeys("1234567890");
    driver.findElement(By.cssSelector("button[name='login']")).click();
  }

  @AfterAll
  static public void stop() {
    driver.quit();
    driver = null;
  }

}