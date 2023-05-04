package ru.stqa.training.selenium.exercise19.tests;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;


import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartTest extends TestBase {
  @Test
  //@Order(2)
  public void cartTest() {
    app.loginUserSite("litecarts@gmail.com", "1234567890");
    app.addCartProducts(3);
    app.eraseAllCart();
    s.assertThat(app.isEmptyCart()).isTrue();
  }
}
