package ru.stqa.training.selenium.exercise14;

import net.lightbody.bmp.core.har.Har;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntry;
import ru.stqa.training.selenium.TestBase;

import java.io.IOException;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WindowTest extends TestBase{

  //@EnabledOnOs(OS.MAC)

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

}