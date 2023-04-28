package ru.stqa.training.selenium.exercise10;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import ru.stqa.training.selenium.TestBase;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest extends TestBase {

  //@EnabledOnOs(OS.MAC)

  @Test
  //@Order(2)
  public void checkingProductTest(){
    driver.get("http://litecart.stqa.ru");

    loginUser("litecarts@gmail.com", "1234567890");
    s.assertThat(driver.findElement(By.cssSelector("div [class='notice success']")).getText()).contains("logged in");

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

}