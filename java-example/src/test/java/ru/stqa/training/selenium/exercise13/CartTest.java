package ru.stqa.training.selenium.exercise13;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import ru.stqa.training.selenium.TestBase;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartTest extends TestBase {

  //@EnabledOnOs(OS.MAC)
  private int quantity;


  @Test
  //@Order(2)
  public void cartTest() {
    driver.get("http://litecart.stqa.ru");
    loginUser("litecarts@gmail.com", "1234567890");
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
    //dataTable = driver.findElements(By.cssSelector("table[class*='dataTable'] tr"));
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

}