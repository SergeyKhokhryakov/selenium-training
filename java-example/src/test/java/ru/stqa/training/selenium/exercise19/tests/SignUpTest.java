package ru.stqa.training.selenium.exercise19.tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import ru.stqa.training.selenium.exercise19.model.Customer;


public class SignUpTest extends TestBase {

  @ParameterizedTest
  @ArgumentsSource(DataProviders.class)
  public void signUpTest(Customer customer){

    app.registerNewCustomer(customer);
    app.logout();
    app.loginUser(customer.getEmail(), customer.getPassword());
    s.assertThat(app.textSuccess())
            .contains("logged in")
            .contains(customer.getFirstname() + " " + customer.getLastname());
    app.logout();
  }
}