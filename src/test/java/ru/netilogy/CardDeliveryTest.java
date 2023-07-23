package ru.netilogy;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class CardDeliveryTest {
    @BeforeAll
    @DisplayName("включение allure")
    static void setUpAll() {
//        SelenideLogger.addListener("allure", new AllureSelenide());
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }
    @AfterAll
    @DisplayName("Выключение allure")
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @Test
    @DisplayName("Should successful plan meeting")
    void shouldSuccessfulPlanMeeting() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateData(daysToAddForFirstMeeting);
        int daysForSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateData(daysForSecondMeeting);

        $("[data-test-id = 'city'] input ").setValue(validUser.getCity());
        $("[data-test-id = 'date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id = 'date'] input").setValue(firstMeetingDate);
        $("[data-test-id ='name'] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $("[data-test-id='success-notification']  .notification__title").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='success-notification']  .notification__content").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $$("button").find(Condition.exactText("Запланировать")).click();
        $("[data-test-id='replan-notification']  .notification__title").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Необходимо подтверждение"));
        $("[data-test-id='replan-notification']  .notification__content").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $$("[data-test-id='replan-notification'] button").find(Condition.exactText("Перепланировать")).click();
        $("[data-test-id='success-notification']  .notification__title").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='success-notification']  .notification__content").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }
}
