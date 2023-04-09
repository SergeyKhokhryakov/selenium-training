package ru.stqa.training.selenium.exercise14;

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

import java.time.Duration;

import static org.assertj.core.util.IterableUtil.toArray;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WindowTest {
  public static WebDriver driver;
  //public static SafariDriver driver;
  public static WebDriverWait wait;
  public static Browser browser;
  private SoftAssertions s = new SoftAssertions();
  //@EnabledOnOs(OS.MAC)
  private int quantity;

  @BeforeAll
  static public void start() {
    browser = Browser.CHROME;
    if (browser.equals(Browser.FIREFOX)) {
      FirefoxOptions options = new FirefoxOptions();
      options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
      driver = new FirefoxDriver(options);
      driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
    } else if (browser.equals(Browser.CHROME)) {
      //Для версии Chrome (chromedriver) 111.0.5563.64 (Официальная сборка), (x86_64)
      ChromeOptions options = new ChromeOptions();
      options.addArguments("--remote-allow-origins=*");
      driver = new ChromeDriver(options);
    } else if (browser.equals(Browser.EDGE)) {
      driver = new EdgeDriver();
    } else if (browser.equals(Browser.SAFARI)) {
      SafariOptions options = new SafariOptions();
      driver = new SafariDriver(options);
    }
    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  @BeforeEach
  public void idle() {

  }

  @Test
  @Order(1)
  public void windowTest() {
    driver.get("http://litecart.stqa.ru");
    String handle = driver.getWindowHandle();
    driver.findElement(By.cssSelector("a[target]")).click();
    String[] handles = driver.getWindowHandles().toArray(new String[0]);
    driver.switchTo().window(handles[1]);
    driver.findElement(By.cssSelector("a[href*='download']")).click();
    s.assertThat(driver.findElement(By.cssSelector("h1")).getText()).isEqualTo("Free Download");
    driver.close();
    s.assertThat(driver.getWindowHandles().size()).isEqualTo(1);
    driver.switchTo().window(handle);
    s.assertThat(driver.getTitle()).contains("My Store");
  }
  @AfterEach
  public void home(){
    s.assertAll();
  }
  @AfterAll
  static public void stop() {
    driver.quit();
    driver = null;
  }

}