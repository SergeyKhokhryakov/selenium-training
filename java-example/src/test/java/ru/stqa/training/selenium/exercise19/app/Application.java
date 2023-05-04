package ru.stqa.training.selenium.exercise19.app;

import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.stqa.training.selenium.exercise19.model.Customer;
import ru.stqa.training.selenium.exercise19.model.Product;
import ru.stqa.training.selenium.exercise19.pages.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.time.Duration;
import java.util.logging.Level;

public class Application {
  private WebDriver driver;
  //public static SafariDriver driver;
  private WebDriverWait wait;
  private RegistrationPage registrationPage;
  private CustomerPanelLoginPage customerPanelLoginPage;
  private CommonPage commonPage;
  private MainPage mainPage;
  private CartPage cartPage;
  private ProductPage productPage;
  private Browser browser;
  private SoftAssertions s = new SoftAssertions();
  public Application () {
    browser = Browser.CHROME;
    if (browser.equals(Browser.FIREFOX)) {
      FirefoxOptions options = new FirefoxOptions();
      options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
      driver = new EventFiringDecorator(new MyListener()).decorate(new FirefoxDriver(options));
      //driver = new FirefoxDriver(options);
      //driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
    } else if (browser.equals(Browser.CHROME)) {
      //Для версии Chrome (chromedriver) 111.0.5563.64 (Официальная сборка), (x86_64)
      ChromeOptions options = new ChromeOptions();
      LoggingPreferences logPrefs = new LoggingPreferences();
      logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
      logPrefs.enable(LogType.BROWSER, Level.ALL);
      options.setCapability("goog:loggingPrefs", logPrefs);
      options.addArguments("--remote-allow-origins=*");

      options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

      driver = new EventFiringDecorator(new MyListener()).decorate(new ChromeDriver(options));

    } else if (browser.equals(Browser.EDGE)) {
      driver = new EventFiringDecorator(new MyListener()).decorate(new EdgeDriver());
      //driver = new EdgeDriver();
    } else if (browser.equals(Browser.SAFARI)) {
      SafariOptions options = new SafariOptions();
      driver = new EventFiringDecorator(new MyListener()).decorate(new SafariDriver(options));
      //driver = new SafariDriver(options);
    }
    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
    wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    registrationPage = new RegistrationPage(driver);
    customerPanelLoginPage = new CustomerPanelLoginPage(driver);
    commonPage = new CommonPage(driver);
    mainPage = new MainPage(driver);
    cartPage = new CartPage(driver);
    productPage = new ProductPage(driver);
  }

  public void loginUser(String email, String password) {
    customerPanelLoginPage.enterEmail(email).enterPassword(password).submitLogin();
  }
  public void loginUserSite(String email, String password){
    customerPanelLoginPage.open().enterEmail(email).enterPassword(password).submitLogin();
  }

  public void logout() {
    registrationPage.logoutLink().click();
  }
  public void home(){
    commonPage.homeLink().click();
  }
  public void registerNewCustomer(Customer customer) {
    registrationPage.open();
    registrationPage.firstnameInput.sendKeys(customer.getFirstname());
    registrationPage.lastnameInput.sendKeys(customer.getLastname());
    registrationPage.address1Input.sendKeys(customer.getAddress1());
    registrationPage.cityInput.sendKeys(customer.getCity());
    registrationPage.selectCountry(customer.getCountry());
    registrationPage.inputPostCode(customer.getPostcode());
    registrationPage.selectZone(customer.getZone());
    registrationPage.emailInput.sendKeys(customer.getEmail());
    registrationPage.inputPhone(customer.getPhone());
    registrationPage.passwordInput.sendKeys(customer.getPassword());
    registrationPage.confirmedPasswordInput.sendKeys(customer.getPassword());
    registrationPage.createAccountButton.click();
  }

  public void addCartProducts(int count) {
    for (int i = 1; i <= count; i++) {
      addCartProduct();
      if (i != count) {
        home();
      }
    }
  }
  private void addCartProduct() {
    mainPage.selectFirstProduct();
    int quantity = Integer.parseInt(commonPage.quantityCart());
    // для "Желтой уточки" необоходимо выбрать обязательное поле Size
    Product duckYellow = new Product();
    if (productPage.nameProduct().equals(duckYellow.getName())) {
      productPage.selectSize(duckYellow.size().value());
    }
    productPage.addToCartButton().click();
    quantity += 1;
    s.assertThat(isElementPresent(driver, commonPage.locatorCart(quantity))).isTrue();
  }

  private boolean isElementPresent(WebDriver driver, By locator){
    try{
      driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
      return driver.findElements(locator).size() > 0;
    }finally{
      driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
    }
  }

  public void eraseAllCart() {
    cartPage.open();
    cartPage.removeFirstProduct();
    cartPage.removeProducts();
    home();
  }
  public boolean isEmptyCart(){
    return commonPage.quantityCart().equals("0");
  }
  public String textSuccess(){
    return commonPage.textSuccess();
  }

  public void quit (){
    driver.quit();
  }
  public class MyListener implements WebDriverListener {

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
      //System.out.println(locator);
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
      //System.out.println(locator +  " found");
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
      System.out.println(e);
      File tempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      File screen = new File("screen-" + System.currentTimeMillis() + ".png");
      try {
        Files.copy(tempFile.toPath(), screen.toPath());
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      System.out.println(screen);
    }
  }
}
