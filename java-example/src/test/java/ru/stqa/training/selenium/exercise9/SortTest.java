package ru.stqa.training.selenium.exercise9;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SortTest {
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
      //driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
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
    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
    if (browser != Browser.FIREFOX) {
      wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
  }
  @BeforeEach
  public void idle(){

  }
  @Test
  @Order(1)
  public void loginTest() {
    driver.get("http://litecart.stqa.ru");
    driver.findElement(By.cssSelector("input[name='email']")).sendKeys("litecarts@gmail.com");
    driver.findElement(By.cssSelector("input[name='password']")).sendKeys("1234567890");
    driver.findElement(By.cssSelector("button[name='login']")).click();
    s.assertThat(driver.findElement(By.cssSelector("div [class='notice success']")).getText()).contains("logged in");
  }

  //@Disabled
  @Test
  @Order(2)
  public void isSortCountryTest(){
    driver.findElement(By.cssSelector(".content .list-vertical a[href*='edit']")).click();
    driver.findElement(By.linkText("Change")).click();
    checkingList(By.xpath("(//select[@name='country_code'])[2]/option"));
    //driver.findElement(By.id("fancybox-close")).click();
  }

  //@Disabled
  @Test
  @Order(3)
  public void isSortZoneTest(){
   // driver.findElement(By.cssSelector(".content .list-vertical a[href*='edit']")).click();
   // driver.findElement(By.linkText("Change")).click();
    WebElement list = driver.findElement(By.xpath("(//select[@name='country_code'])[2]"));
    int length = driver.findElements(By.xpath("(//select[@name='country_code'])[2]/option")).size();
    Select select = new Select(list);
    for (int i = 1; i <= length-1; i++) {
      try {
        select.selectByIndex(i);
        checkingList(By.xpath("(//select[@name='zone_code'])[1]/option"));
      } catch (StaleElementReferenceException ex) {
        driver.findElement(By.xpath("(//select[@name='country_code'])[2]"));
        checkingList(By.xpath("(//select[@name='zone_code'])[1]/option"));
      }
    }
   // driver.findElement(By.id("fancybox-close")).click();
  }

  protected void checkingList(By locator) {
    List<WebElement> elements = driver.findElements(locator);
    if (elements.size() != 0) {
      ArrayList<String> countries1 = new ArrayList<>();
      ArrayList<String> countries2 = new ArrayList<>();
      for (WebElement zone : elements) {
        String str = zone.getText();
        countries1.add(str);
        countries2.add(str);
      }
      Collections.sort(countries2);
      s.assertThat(countries1).isEqualTo(countries2);
    }
  }

  @AfterEach
  public void home(){
    s.assertAll();
   // driver.findElement(By.id("fancybox-close")).click();
   // driver.findElement(By.cssSelector("#site-menu .general-0 a")).click();
  }
  @AfterAll
  static public void stop() {
    driver.findElement(By.id("fancybox-close")).click();
    driver.findElement(By.cssSelector("#site-menu .general-0 a")).click();
    driver.quit();
    driver = null;
  }

}