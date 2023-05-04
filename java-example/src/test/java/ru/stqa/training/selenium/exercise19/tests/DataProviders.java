package ru.stqa.training.selenium.exercise19.tests;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.stqa.training.selenium.exercise19.model.Customer;

import java.util.stream.Stream;

/**
 *  Набор входных данных для тестирования функции регистрации нового пользователя в приложении
 *  Уникальность пользователя реализуется рандомным значением email
 *  <p>
 *      1. параметр Country, для примера, может принимать следующие значения: <br>
 *        - US (США), при этом код географической зоны м.б. AA, GA, ME ... (см. код страницы) <br>
 *        - ML (Мали), код зоны - заблокирован <br>
 *        - RU (Россия), код зоны - заблокирован <br>
 *        - LB (Ливан), код зоны - заблокирован <br>
 *        - CA (Канада), при этом код географической зоны м.б. AB, NT, YT ... (см. код страницы) <br>
 *        - и т.д. (см. код страницы)
 *      <p>
 *       2. значение параметра Postcode автоматически генерируется при заполнении формы на основе предустановленного шаблона (регулярного выражения),
 *       соответствующего выбранной страны. Если шаблон отсутствует, то форма заполняется текущим значением из набора (например, "MyPostCode-14212").
 *    @author Сергей Хохряков
 */
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
