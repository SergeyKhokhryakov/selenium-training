package ru.stqa.training.selenium.exercise17;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.stqa.training.selenium.TestBase;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by user on 28.04.2023.
 */

/*
        Сделайте сценарий, который проверяет, не появляются ли сообщения об ошибках при открытии страниц в учебном приложении, а именно -- страниц товаров в каталоге главной панели.
        Сценарий должен состоять из следующих частей:
        1) зайти под учеткой,
        2) открыть каталог, категорию, которая содержит товары (страница https://litecart.stqa.ru/en/rubber-ducks-c-1/),
        3) последовательно открывать страницы товаров и проверять, не появляются ли в логе браузера сообщения об ошибках (любого уровня критичности).
 */


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogBrowserTest extends TestBase {


  @Test
  @Order(1)
  public void logBrowserTest() throws IOException {
    driver.get("http://litecart.stqa.ru");
    loginUser("litecarts@gmail.com", "1234567890");
    String handle = driver.getWindowHandle();

    driver.switchTo().newWindow(WindowType.TAB);
    driver.get("https://litecart.stqa.ru/en/rubber-ducks-c-1/");
    List<WebElement> elements = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("ul.products>li"), 3));
    for (int index = 0; index < elements.size(); index++) {
      wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("ul.products>li"), 3))
              .get(index)
              .findElement(By.cssSelector("a.link"))
              .click();
      //lets check log warnings or severe
      for (LogEntry l : driver.manage().logs().get("browser").getAll()) {
        if (l.getLevel() == Level.WARNING || l.getLevel() == Level.SEVERE) {
          System.out.println(l);
        }
      }
      driver.navigate().back();
    }
    driver.close();

    driver.switchTo().window(handle);
    s.assertThat(driver.getTitle()).contains("My Store");
  }

}