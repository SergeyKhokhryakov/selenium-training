package ru.stqa.training.selenium.exercise13;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartTest {
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
  public void loginTest() {
    driver.get("http://litecart.stqa.ru");
    driver.findElement(By.cssSelector("input[name='email']")).sendKeys("litecarts@gmail.com");
    driver.findElement(By.cssSelector("input[name='password']")).sendKeys("1234567890");
    driver.findElement(By.cssSelector("button[name='login']")).click();
    s.assertThat(driver.findElement(By.cssSelector("#notices i")).getText()).isSubstringOf("logged in");
  }

  @Test
  @Order(2)
  public void cartTest() {
    addCartProducts(3);
    removeAllCart();
    driver.findElement(By.cssSelector("#site-menu .general-0 a")).click();
    // корзина пуста
    s.assertThat(driver.findElement(By.xpath("//div[@id='cart']//span[@class='quantity'][.='0']")).getText()).isEqualTo("0");
  }

  private void removeAllCart() {
    driver.findElement(By.cssSelector("#cart a[class='link']")).click();
    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[name='remove_cart_item']"))); // ожидание интерактивности элемента

    List<WebElement> dataTable = driver.findElements(By.cssSelector("table[class*='dataTable'] tr"));
    driver.findElement(By.cssSelector("button[name='remove_cart_item']")).click();// после удаления товара слайдирование товаров останавливается
    wait.until(ExpectedConditions.stalenessOf(dataTable.get(dataTable.size()-1))); // ожидание обновления таблицы внизу страницы
    dataTable = driver.findElements(By.cssSelector("table[class*='dataTable'] tr"));
    List<WebElement> elements = driver.findElements(By.cssSelector("#checkout-cart-wrapper ul[class='items']>li"));
    while (elements.size() != 0){
      elements.get(0).findElement(By.cssSelector("button[name='remove_cart_item']")).click();
      wait.until(ExpectedConditions.stalenessOf(dataTable.get(dataTable.size()-1))); // ожидание обновления таблицы внизу страницы
      wait.until(ExpectedConditions.stalenessOf(elements.get(elements.size()-1))); // ожидание обновления основной таблицы корзины
      dataTable = driver.findElements(By.cssSelector("table[class*='dataTable'] tr"));
      elements = driver.findElements(By.cssSelector("#checkout-cart-wrapper ul[class='items']>li"));
    }
  }

  private void addCartProducts(int count) {
    for (int i = 1; i <= count; i++) {
      addCartProduct();
      if (i != count) {
        driver.findElement(By.cssSelector("#site-menu .general-0 a")).click();
      }
    }
  }

  private void addCartProduct() {
    List<WebElement> elements = driver.findElements(By.cssSelector("#box-most-popular .products>li"));
    if (elements.size() > 0){
      elements.get(0).click();
    }
    quantity = Integer.parseInt(driver.findElement(By.xpath("//div[@id='cart']//span[@class='quantity']")).getText());
    if (driver.findElement(By.cssSelector("h1[class='title'")).getText().equals("Yellow Duck")) {
      new Select(driver.findElement(By.cssSelector("select[name='options[Size]']"))).selectByIndex(1);
    }
    driver.findElement(By.cssSelector("button[name='add_cart_product']")).click();
    quantity += 1;
    s.assertThat(isElementPresent(driver, By.xpath("//div[@id='cart']//span[@class='quantity'][.="+quantity+"]"))).isTrue();
  }

  private boolean isElementPresent(WebDriver driver, By locator){
    try{
      driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
      return driver.findElements(locator).size() > 0;
    }finally{
      driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    }
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