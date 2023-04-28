package ru.stqa.training.selenium.exercise9;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.stqa.training.selenium.TestBase;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StickerTest extends TestBase {

  @Test
  @Order(1)
  public void loginTest() {
    driver.get("http://litecart.stqa.ru");
    loginUser("litecarts@gmail.com", "1234567890");
    s.assertThat(driver.findElement(By.cssSelector("div [class='notice success']")).getText()).contains("logged in");
  }

  @Test
  @Order(2)
  public void mostPopularTest(){
    isOnlyOneSticker(By.cssSelector("#box-most-popular .products>li"));
  }

  @Test
  @Order(3)
  public void campaignsTest(){
    isOnlyOneSticker(By.cssSelector("#box-campaigns .products>li"));
  }

  @Test
  @Order(4)
  public void latestProductsTest(){
    isOnlyOneSticker(By.cssSelector("#box-latest-products .products>li"));
  }

  private void isOnlyOneSticker(By locator) {
    List<WebElement> elements = driver.findElements(locator);
    for (WebElement element : elements){
      int stickerCount = element.findElements(By.cssSelector(".sticker")).size();
      if (stickerCount > 0) {
        s.assertThat(stickerCount).isEqualTo(1);
      }
    }
  }


}