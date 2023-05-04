package ru.stqa.training.selenium.exercise19.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CartPage extends Page {

  public  CartPage (WebDriver driver) {
    super(driver);
  }
  public void open(){
    driver.findElement(By.cssSelector("#cart a[class='link']")).click();
  }

  By locatorItemCart(){
    return By.cssSelector("button[name='remove_cart_item']");
  }
  public WebElement removeItemButton(){
    return driver.findElement(locatorItemCart());
  }

  public void removeFirstProduct(){
    // ожидание интерактивности элемента
    wait.until(ExpectedConditions.elementToBeClickable(locatorItemCart()));
    List<WebElement> dataTable = driver.findElements(By.cssSelector("table[class*='dataTable'] tr"));
    removeItemButton().click();
    // после удаления товара слайдирование товаров останавливается
    // ожидание обновления таблицы внизу страницы
    wait.until(ExpectedConditions.stalenessOf(dataTable.get(dataTable.size()-1)));
  }
  public WebElement currentProduct(){
    return driver.findElement(By.cssSelector("button[name='remove_cart_item']"));
  }

  public void removeProducts(){
    List<WebElement> dataTable = driver.findElements(By.cssSelector("table[class*='dataTable'] tr"));
    List<WebElement> elements = driver.findElements(By.cssSelector("#checkout-cart-wrapper ul[class='items']>li"));
    while (elements.size() != 0){
      elements.get(0).findElement(locatorItemCart()).click();
      wait.until(ExpectedConditions.stalenessOf(dataTable.get(dataTable.size()-1))); // ожидание обновления таблицы внизу страницы
      wait.until(ExpectedConditions.stalenessOf(elements.get(elements.size()-1))); // ожидание обновления основной таблицы корзины
      dataTable = driver.findElements(By.cssSelector("table[class*='dataTable'] tr"));
      elements = driver.findElements(By.cssSelector("#checkout-cart-wrapper ul[class='items']>li"));
    }
  }
}
