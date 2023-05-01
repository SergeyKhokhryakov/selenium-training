package ru.stqa.training.selenium.exercise19;

import com.mifmif.common.regex.Generex;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignUpTest extends TestBase {

  //@EnabledOnOs(OS.MAC)

  @ParameterizedTest
  @ArgumentsSource(DataProviders.class)
  public void signUpTest(Customer customer){

    driver.get("http://litecart.stqa.ru");

    driver.findElement(By.cssSelector(".content a[href*='create_account']")).click();
    driver.findElement(By.cssSelector("input[name='firstname']")).sendKeys(customer.getFirstname());
    driver.findElement(By.cssSelector("input[name='lastname']")).sendKeys(customer.getLastname());
    driver.findElement(By.cssSelector("input[name='address1']")).sendKeys(customer.getAddress1());
    driver.findElement(By.cssSelector("input[name='city']")).sendKeys(customer.getCity());

    driver.findElement(By.cssSelector("[id ^= 'select2-country_code']")).click();
    driver.findElement(By.cssSelector(".select2-results__option[id $= '" + customer.getCountry() + "']")).click();

    String pattern = driver.findElement(By.cssSelector("input[name='postcode']")).getAttribute("pattern");
    if (pattern.length() == 0){
      driver.findElement(By.cssSelector("input[name='postcode']")).sendKeys(customer.getPostcode());
    } else {
      Generex generex = new Generex(pattern);
      String postCode = generex.random();
      driver.findElement(By.cssSelector("input[name='postcode']")).sendKeys(postCode);
    }

    wait.until((WebDriver wd) -> wd.findElements(By.xpath("//select[@name='zone_code']/option")));
    int length = driver.findElements(By.xpath("//select[@name='zone_code']/option")).size();
    if (length != 0){
      new Select(driver.findElement(By.xpath("//select[@name='zone_code']"))).selectByValue(customer.getZone());
    }

    driver.findElement(By.cssSelector("input[name='email']")).sendKeys(customer.getEmail());
    String code = driver.findElement(By.cssSelector("input[name='phone']")).getAttribute("placeholder");
    driver.findElement(By.cssSelector("input[name='phone']")).sendKeys(code+customer.getPhone());

    driver.findElement(By.cssSelector("input[name='password']")).sendKeys(customer.getPassword());
    driver.findElement(By.cssSelector("input[name='confirmed_password']")).sendKeys(customer.getPassword());
    driver.findElement(By.cssSelector("button[name='create_account']")).click();

    logout();

    loginUser(customer.getEmail(), customer.getPassword());
    s.assertThat(driver.findElement(By.cssSelector("div [class='notice success']")).getText()).contains("logged in");
    logout();
  }

}