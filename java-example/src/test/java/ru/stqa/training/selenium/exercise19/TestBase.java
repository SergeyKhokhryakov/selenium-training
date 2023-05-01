package ru.stqa.training.selenium.exercise19;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.logging.Level;

public class TestBase {
  public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
  protected static WebDriver driver;
  //public static SafariDriver driver;
  protected static WebDriverWait wait;
  protected static Browser browser;
  protected static BrowserMobProxy proxy;
  protected static Proxy seleniumProxy;
  protected SoftAssertions s = new SoftAssertions();

  @BeforeAll
  static public void start() throws UnknownHostException {
    if (tlDriver.get() != null){
      driver = tlDriver.get();
      wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    // start the proxy
    proxy = new BrowserMobProxyServer();
    // proxy.setTrustAllServers(true);
    proxy.start(8080);
    // get the Selenium proxy object
    seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
    String hostIp = Inet4Address.getLocalHost().getHostAddress();
    seleniumProxy.setHttpProxy(hostIp + ":" + proxy.getPort());
    seleniumProxy.setSslProxy(hostIp + ":" + proxy.getPort());
    proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

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


      //DesiredCapabilities capabilities = new DesiredCapabilities();
      //capabilities.setCapability(CapabilityType.PROXY, seleniumProxy); // пустая команда - прокси не подключается, все работает напрямую
      //capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
      /*
      options.addArguments("--disable-web-security");
      options.addArguments("--allow-insecure-localhost");
      options.addArguments("--ignore-urlfetcher-cert-requests");
       */
      options.setCapability(CapabilityType.PROXY, seleniumProxy);
      options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
      //capabilities.setCapability(ChromeOptions.CAPABILITY, options);
      //options.merge(capabilities);
      driver = new EventFiringDecorator(new MyListener()).decorate(new ChromeDriver(options));
      tlDriver.set(driver);

      //driver = new ChromeDriver(options);
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
    Runtime.getRuntime().addShutdownHook(
            new Thread(() -> { driver.quit(); driver = null;})
    );
  }

  @AfterAll
  static public void stop() {
    //driver.quit();
    proxy.stop();
    //driver = null;
  }

  @BeforeEach
  public void idle() {

  }


  public void loginUser(String email, String password) {
    driver.findElement(By.cssSelector("input[name='email']")).sendKeys(email);
    driver.findElement(By.cssSelector("input[name='password']")).sendKeys(password);
    driver.findElement(By.cssSelector("button[name='login']")).click();
  }

  protected static void logout() {
    driver.findElement(By.cssSelector(".content .list-vertical a[href*='logout']")).click();
  }

  @AfterEach
  public void home(){
    s.assertAll();
    driver.findElement(By.cssSelector("#site-menu .general-0 a")).click();
  }

  public static class MyListener implements WebDriverListener {

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
