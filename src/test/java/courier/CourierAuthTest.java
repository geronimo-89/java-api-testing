package courier;

import client.CourierClient;
import data.CourierData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import pojo.courier.*;

import static client.CourierClient.*;
import static pojo.courier.Courier.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Авторизация курьера")
public class CourierAuthTest extends CourierData {

    static Courier courier;
    static CourierClient client;
    int expectedStatusCode;
    static CourierId id;


    @BeforeClass
    @Step("Создание курьера и клиента для запросов с ним")
    public static void setUp() {

        client = new CourierClient();
        courier = createRandom(testPassword);
        client.register(courier)
                .statusCode(201);
    }

    @Test
    @DisplayName("Курьер с валидной парой логин-пароль может авторизоваться")
    @Description("Ожидаемый код ответа: 200, запрос возвращает ID")
    public void shouldAuthWithValidCredentials() {

        expectedStatusCode = 200;

        client.login(courier)
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

        client.login(courier)
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

        client.login((createRandom(testPassword)))
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

        client.login(passwordFrom(courier))
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

        client.login(loginFrom(courier))
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getLoginMessage(expectedStatusCode)));

    }

    @AfterClass
    @Step("Удаление из базы тестового курьера")
    public static void cleanUp() {
        id = client.getCourierId(courier);
        if (id != null) {
            client.delete(id)
                    .statusCode(200);
        }
    }


}
