package courier;

import client.CourierClient;
import data.CourierData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.courier.*;

import static client.CourierClient.getRegisterMessage;
import static client.ScooterBaseClient.*;
import static pojo.courier.Courier.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Регистрация курьера")
public class CourierRegisterTest extends CourierData {

    Courier courier;
    Courier existingCourier;
    CourierClient client;
    int expectedStatusCode;

    @Before
    @Step("Создание курьера и клиента для запросов с ним")
    public void setUp() {

        client = new CourierClient();
        courier = createRandom(testPassword);
    }

    @Test
    @DisplayName("Можно зарегистрировать нового курьера с логином, паролем и именем")
    @Description("Ожидаемый код ответа: 201")
    public void shouldCreateCourierWithAllParams() {

        expectedStatusCode = 201;

        client.register(courier)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("ok", is(getOk(expectedStatusCode)));
    }


    @Test
    @DisplayName("Можно зарегистрировать нового курьера с логином и паролем, без имени")
    @Description("Ожидаемый код ответа: 201")
    public void shouldCreateCourierWithoutFirstName() {

        expectedStatusCode = 201;

        client.register(loginPasswordFrom(courier))
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("ok", is(getOk(expectedStatusCode)));
    }

    @Test
    @DisplayName("Нельзя зарегистрировать курьера с уже зарегистрированным логином")
    @Description("Ожидаемый код ответа: 409")
    public void shouldNotCreateCourierWithExistingLogin() {

        expectedStatusCode = 409;

        existingCourier = courier;
        client.register(existingCourier)
                .statusCode(201);

        courier = createWithExistingLogin(existingCourier);

        client.register(courier)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getRegisterMessage(expectedStatusCode)));

    }

    @Test
    @DisplayName("Нельзя зарегистрировать курьера без обязательного поля логина")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotCreateCourierWithoutLogin() {

        expectedStatusCode = 400;

        client.register(withoutLoginFrom(courier))
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getRegisterMessage(expectedStatusCode)));
    }

    @Test
    @DisplayName("Нельзя зарегистрировать курьера без обязательного поля пароля")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotCreateCourierWithoutPassword() {

        expectedStatusCode = 400;

        client.register(withoutPasswordFrom(courier))
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getRegisterMessage(expectedStatusCode)));
    }

    @After
    @Step("Удаление из базы тестовых курьеров")
    public void cleanUp() {

        if (expectedStatusCode == 201) {
            if (courier != null) {
                client.delete(client.getCourierId(courier))
                        .statusCode(200);
            }
            if (existingCourier != null) {
                client.delete(client.getCourierId(existingCourier))
                        .statusCode(200);
            }
        }
    }


}
