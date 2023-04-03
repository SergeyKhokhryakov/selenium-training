package ru.stqa.training.selenium.exercise10;

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
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest {
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
  @Order(1)
  public void loginTest() {
    driver.get("http://litecart.stqa.ru");
    driver.findElement(By.cssSelector("input[name='email']")).sendKeys("litecarts@gmail.com");
    driver.findElement(By.cssSelector("input[name='password']")).sendKeys("1234567890");
    driver.findElement(By.cssSelector("button[name='login']")).click();
    s.assertThat(driver.findElement(By.cssSelector("#notices i")).getText()).isSubstringOf("logged in");
  }

  @Test
  @Order(2)
  public void checkingProductTest(){
    List<WebElement> elements = driver.findElements(By.cssSelector("#box-campaigns ul>li"));
    String name = elements.get(0).findElement(By.cssSelector(".name")).getText();
    WebElement element = elements.get(0).findElement(By.cssSelector("#box-campaigns .regular-price"));
    // Первая цена (regular-price) товара - серая, зачёркнутая, маленькая
    String price = element.getText();
    checkingFirstPrice(element);

    // вторая цена (campaign-price) красная жирная, крупная
    element = elements.get(0).findElement(By.cssSelector("#box-campaigns .campaign-price"));
    String priceCompany = element.getText();
    checkingSecondPrice(element);

    elements.get(0).click();
    s.assertThat(driver.findElement(By.cssSelector("h1")).getText()).isEqualTo(name);
    element = driver.findElement(By.cssSelector("#box-product .regular-price"));
    s.assertThat(element.getText()).isEqualTo(price);
    checkingFirstPrice(element);
    element = driver.findElement(By.cssSelector("#box-product .campaign-price"));
    s.assertThat(element.getText()).isEqualTo(priceCompany);
    checkingSecondPrice(element);
  }

  private void checkingFirstPrice(WebElement element) {
    // Оттенок серого #808080 (оригинал) верхняя граница - #999999, нижняя граница - #666666
    // https://www.color-hex.com/color/808080
    // Оттенок серого 25% - #77777/#777
    String priceColor = (Color.fromString(element.getCssValue("color")).asHex()).substring(1);
    int decimal = Integer.parseInt(priceColor, 16);
    //s.assertThat(priceColor).isEqualTo("#777777");
    s.assertThat(decimal).isBetween(Integer.parseInt("666666", 16), Integer.parseInt("999999", 16));
    // текст зачеркнутый
    s.assertThat(element.getCssValue("text-decoration-line")).isEqualTo("line-through");
    // размер маленький: small (13px) - medium (16px)
    float size = Float.parseFloat(element.getCssValue("font-size").split("px")[0]);
    s.assertThat(size).isBetween(13.0F, 16.0F);
  }
  private void checkingSecondPrice(WebElement element) {
    // красный #cc0000
    String priceColor = Color.fromString(element.getCssValue("color")).asHex();
    s.assertThat(priceColor).isEqualTo("#cc0000");
    // жирная font-weight: bold (700)
    s.assertThat(element.getCssValue("font-weight")).isEqualTo("700");
    // крупный текст: large (18px) x-large (24px)
    float size = Float.parseFloat(element.getCssValue("font-size").split("px")[0]);
    s.assertThat(size).isBetween(18.0F, 24.0F);
  }

  @AfterEach
  public void home(){
    s.assertAll();
    driver.findElement(By.cssSelector("#site-menu .general-0 a")).click();
  }
  @AfterAll
  static public void stop() {
    driver.quit();
    driver = null;
  }

}