package ru.stqa.training.selenium.exercise19.tests;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import ru.stqa.training.selenium.exercise19.app.Application;

public class TestBase {
  public static ThreadLocal<Application> tlApp = new ThreadLocal<>();
  public static Application app;

  protected SoftAssertions s = new SoftAssertions();

  @BeforeAll
  public static void start() {
    if (tlApp.get() != null){
      app = tlApp.get();
      return;
    }

    app = new Application();
    tlApp.set(app);

    Runtime.getRuntime().addShutdownHook(
            new Thread(() -> { app.quit(); app = null;})
    );
  }

  @AfterEach
  public void home(){
    s.assertAll();
    app.home();
  }

}
