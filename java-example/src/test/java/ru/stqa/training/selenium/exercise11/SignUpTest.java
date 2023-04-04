package ru.stqa.training.selenium.exercise11;

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
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignUpTest {
  public static WebDriver driver;
  //public static SafariDriver driver;
  public static WebDriverWait wait;
  public static Browser browser;
  private SoftAssertions s = new SoftAssertions();
  //@EnabledOnOs(OS.MAC)
  @BeforeAll
  static public void start() {
    browser = Browser.CHROME;
    if (browser.equals(Browser.FIREFOX)){
      FirefoxOptions options = new FirefoxOptions();
      options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
      driver = new FirefoxDriver(options);
      driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
    } else if (browser.equals(Browser.CHROME)){
      //Для версии Chrome (chromedriver) 111.0.5563.64 (Официальная сборка), (x86_64)
      ChromeOptions options = new ChromeOptions();
      options.addArguments("--remote-allow-origins=*");
      driver = new ChromeDriver(options);
    } else if (browser.equals(Browser.EDGE)){
      driver = new EdgeDriver();
    } else if (browser.equals(Browser.SAFARI)){
      SafariOptions options = new SafariOptions();
      driver = new SafariDriver(options);
    }
    if (browser != Browser.FIREFOX) {
      wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
  }
  @BeforeEach
  public void idle(){

  }

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

    driver.findElement(By.cssSelector(".content .list-vertical a[href*='logout']")).click();

    driver.findElement(By.cssSelector("input[name='email']")).sendKeys(email);
    driver.findElement(By.cssSelector("input[name='password']")).sendKeys(password);
    driver.findElement(By.cssSelector("button[name='login']")).click();

    driver.findElement(By.cssSelector(".content .list-vertical a[href*='logout']")).click();
  }

  @AfterEach
  public void home(){
    //s.assertAll();
    //driver.findElement(By.cssSelector("#site-menu .general-0 a")).click();
  }
  @AfterAll
  static public void stop() {
    driver.quit();
    driver = null;
  }

}