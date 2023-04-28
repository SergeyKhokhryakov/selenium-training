package ru.stqa.training.selenium.exercise14;



import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarRequest;
import net.lightbody.bmp.core.har.HarResponse;
import net.lightbody.bmp.mitm.KeyStoreFileCertificateSource;
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager;
import net.lightbody.bmp.proxy.CaptureType;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
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
import java.util.List;
import java.util.logging.Level;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WindowTest {
  public static WebDriver driver;
  //public static SafariDriver driver;
  public static WebDriverWait wait;

  static BrowserMobProxy proxy;
  static Proxy seleniumProxy;
  public static Browser browser;
  private SoftAssertions s = new SoftAssertions();
  //@EnabledOnOs(OS.MAC)
  private int quantity;

  public static class MyListener implements WebDriverListener {

    @Override
    public void beforeFindElement(WebDriver driver, By locator) {
      System.out.println(locator);
    }

    @Override
    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
      System.out.println(locator +  " found");
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

  @BeforeAll
  static public void start() throws UnknownHostException {
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
      options.setCapability("goog:loggingPrefs", logPrefs);
      options.addArguments("--remote-allow-origins=*");
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

      //driver = new ChromeDriver(options);
    } else if (browser.equals(Browser.EDGE)) {
      driver = new EventFiringDecorator(new MyListener()).decorate(new EdgeDriver());
      //driver = new EdgeDriver();
    } else if (browser.equals(Browser.SAFARI)) {
      SafariOptions options = new SafariOptions();
      driver = new EventFiringDecorator(new MyListener()).decorate(new SafariDriver(options));
      //driver = new SafariDriver(options);
    }
    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));
    wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  }

  @BeforeEach
  public void idle() {

  }


  @Test
  @Order(1)
  public void windowTest() throws IOException {
    //proxy.newHar("litecart.stqa.ru");
    proxy.newHar();
    driver.get("http://litecart.stqa.ru");

    Har har = proxy.endHar();
    har.getLog().getEntries().forEach(l -> System.out.println(l.getResponse().getStatus() + ":" + l.getRequest().getUrl()));

    //har.writeTo(new File("litecart.har"));

    /*
    List<HarEntry> list = har.getLog().getEntries();
    for (HarEntry l : list){
      System.out.println(l.getResponse().getStatus() + ":" + l.getRequest().getUrl());
    }
     */

    System.out.println(driver.manage().logs().getAvailableLogTypes());
    for (LogEntry l : driver.manage().logs().get("performance").getAll()) {
      System.out.println(l);
    }

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
    proxy.stop();
    driver = null;
  }

}