package courier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import setup.SetUpTests;

import static client.CourierClient.getLoginMessage;
import static data.CourierData.testPassword;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static pojo.courier.Courier.*;

@DisplayName("Авторизация курьера")
public class CourierAuthTest extends SetUpTests {

    @Before
    @Step("Создание курьера и клиента для запросов с ним")
    public void setUp() {
        setUpCourier();
    }

    @Test
    @DisplayName("Курьер с валидной парой логин-пароль может авторизоваться")
    @Description("Ожидаемый код ответа: 200, запрос возвращает ID")
    public void shouldAuthWithValidCredentials() {

        expectedStatusCode = 200;

        courierClient.login(courier)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("id", is(notNullValue()));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с невалидной парой логин-пароль")
    @Description("Ожидаемый код ответа: 404")
    public void shouldNotAuthWithIncorrectCredentials() {

        expectedStatusCode = 404;
        courier.setRandomPassword(12);

        courierClient.login(courier)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getLoginMessage(expectedStatusCode)));
    }

    @Test
    @DisplayName("Нельзя авторизоваться под незарегистрированным курьером")
    @Description("Ожидаемый код ответа: 404")
    public void shouldNotAuthWithUnregisteredCredentials() {

        expectedStatusCode = 404;

        courierClient.login((createRandom(testPassword)))
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getLoginMessage(expectedStatusCode)));

    }

    @Test
    @DisplayName("Курьер не может авторизоваться без логина")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotAuthWithoutLogin() {

        expectedStatusCode = 400;

        courierClient.login(passwordFrom(courier))
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getLoginMessage(expectedStatusCode)));

    }

    @Test
    @DisplayName("Курьер не может авторизоваться без пароля")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotAuthWithoutPassword() {

        expectedStatusCode = 400;

        courierClient.login(loginFrom(courier))
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getLoginMessage(expectedStatusCode)));

    }

    @After
    @Step("Удаление из базы тестовых курьеров")
    public void cleanUp() {
            cleanUpCourier();
    }


}
