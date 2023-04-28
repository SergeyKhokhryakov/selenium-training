package ru.stqa.training.selenium.exercise8;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LeftPanelTest extends TestBase {

  @Test
  @Order(1)
  public void loginTest() {
    driver.get("http://litecart.stqa.ru");
    loginUser("litecarts@gmail.com", "1234567890");
    s.assertThat(driver.findElement(By.cssSelector("div [class='notice success']")).getText()).contains("logged in");
  }

  @Test
  @Order(2)
  public void categoriesTest(){
    driver.findElement(By.cssSelector(".list-vertical .category-1>a")).click();
    s.assertThat(driver.findElement(By.cssSelector("h1.title")).getText()).isEqualTo("Rubber Ducks");
    driver.findElement(By.cssSelector(".list-vertical .category-2>a")).click();
    s.assertThat(driver.findElement(By.cssSelector("h1.title")).getText()).isEqualTo("Subcategory");
  }

  @Test
  @Order(3)
  public void accountCustomerServiceTest(){
    driver.findElement(By.cssSelector(".content .list-vertical a[href*='customer']")).click();
    s.assertThat(driver.findElement(By.cssSelector("h3.title")).getText()).isEqualTo("Customer Service");
    s.assertThat(driver.findElement(By.cssSelector("h1.title")).getText()).isEqualTo("Contact Us");
    driver.findElement(By.cssSelector(".content .list-vertical a[href*='about']")).click();
    s.assertThat(driver.findElement(By.cssSelector(".content h1")).getText()).isEqualTo("About Us");
    s.assertThat(driver.findElement(By.cssSelector(".content h2")).getText()).isEqualTo("Subheading 2");
    s.assertThat(driver.findElement(By.cssSelector(".content h3")).getText()).isEqualTo("Subheading 3");
    driver.findElement(By.cssSelector(".content .list-vertical a[href*='delivery']")).click();
    s.assertThat(driver.findElement(By.cssSelector(".content h1")).getText()).isEqualTo("Delivery Information");
    s.assertThat(driver.findElement(By.cssSelector(".content h2")).getText()).isEqualTo("Subheading 2");
    s.assertThat(driver.findElement(By.cssSelector(".content h3")).getText()).isEqualTo("Subheading 3");
    driver.findElement(By.cssSelector(".content .list-vertical a[href*='privacy']")).click();
    s.assertThat(driver.findElement(By.cssSelector(".content h1")).getText()).isEqualTo("Privacy Policy");
    s.assertThat(driver.findElement(By.cssSelector(".content h2")).getText()).isEqualTo("Subheading 2");
    s.assertThat(driver.findElement(By.cssSelector(".content h3")).getText()).isEqualTo("Subheading 3");
    driver.findElement(By.cssSelector(".content .list-vertical a[href*='terms']")).click();
    s.assertThat(driver.findElement(By.cssSelector(".content h1")).getText()).isEqualTo("Terms & Conditions");
    s.assertThat(driver.findElement(By.cssSelector(".content h2")).getText()).isEqualTo("Subheading 2");
    s.assertThat(driver.findElement(By.cssSelector(".content h3")).getText()).isEqualTo("Subheading 3");
  }

  @Test
  @Order(4)
  public void orderHistoryTest(){
    driver.findElement(By.cssSelector(".content .list-vertical a[href*='order']")).click();
    s.assertThat(driver.findElement(By.cssSelector("h1.title")).getText()).isEqualTo("Order History");
  }
  @Test
  @Order(5)
  public void editAccountTest(){
    driver.findElement(By.cssSelector(".content .list-vertical a[href*='edit']")).click();
    s.assertThat(driver.findElement(By.cssSelector("h1.title")).getText()).isEqualTo("Edit Account");
  }
  @Test
  @Order(6)
  public void logoutTest() {
    logout();
    s.assertThat(driver.findElement(By.cssSelector("#notices i")).getText()).isSubstringOf("logged out");
  }

}