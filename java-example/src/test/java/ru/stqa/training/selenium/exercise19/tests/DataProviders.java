package ru.stqa.training.selenium.exercise19.tests;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.stqa.training.selenium.exercise19.model.Customer;

import java.util.stream.Stream;

public class DataProviders implements ArgumentsProvider {
  @Override
  public Stream <? extends Arguments> provideArguments (ExtensionContext context) {
    int index = (int) (Math.random() * 100);
    return Stream.of( Customer.newEntity()
                .withFirstname("Igor").withLastname("Prigozyn").withPhone("9111234419")
                .withAddress1("street Tverskaya, 10-2-187").withPostcode("MyPostCode-14212").withCity("Moscow")
                .withCountry("US").withZone("GA")
                .withEmail("email_" + index + "@gmail.com")
                .withPassword("12345").build()).map(Arguments::of);
  }
}
