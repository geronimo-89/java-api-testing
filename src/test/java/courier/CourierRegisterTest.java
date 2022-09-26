package courier;

import client.CourierClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.courier.Courier;
import setup.SetUpTests;

import static data.CourierData.testPassword;
import static org.hamcrest.Matchers.is;
import static pojo.courier.Courier.*;

@DisplayName("Регистрация курьера")
public class CourierRegisterTest extends SetUpTests {

    private static final String REGISTER_400 = "Недостаточно данных для создания учетной записи";
    private static final String REGISTER_409 = "Этот логин уже используется. Попробуйте другой.";

    Courier existingCourier;

    @Before
    @Step("Создание курьера и клиента для запросов с ним")
    public void setUp() {

        courierClient = new CourierClient();
        courier = createRandom(testPassword);
    }

    @Test
    @DisplayName("Можно зарегистрировать нового курьера с логином, паролем и именем")
    @Description("Ожидаемый код ответа: 201")
    public void shouldCreateCourierWithAllParams() {

        expectedStatusCode = 201;

        courierClient.register(courier)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Можно зарегистрировать нового курьера с логином и паролем, без имени")
    @Description("Ожидаемый код ответа: 201")
    public void shouldCreateCourierWithoutFirstName() {

        expectedStatusCode = 201;

        courierClient.register(loginPasswordFrom(courier))
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Нельзя зарегистрировать курьера с уже зарегистрированным логином")
    @Description("Ожидаемый код ответа: 409")
    public void shouldNotCreateCourierWithExistingLogin() {

        expectedStatusCode = 409;

        existingCourier = courier;
        courierClient.register(existingCourier)
                .statusCode(201);

        courier = createWithExistingLogin(existingCourier);

        courierClient.register(courier)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(REGISTER_409));

    }

    @Test
    @DisplayName("Нельзя зарегистрировать курьера без обязательного поля логина")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotCreateCourierWithoutLogin() {

        expectedStatusCode = 400;

        courierClient.register(withoutLoginFrom(courier))
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(REGISTER_400));
    }

    @Test
    @DisplayName("Нельзя зарегистрировать курьера без обязательного поля пароля")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotCreateCourierWithoutPassword() {

        expectedStatusCode = 400;

        courierClient.register(withoutPasswordFrom(courier))
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(REGISTER_400));
    }

    @After
    @Step("Удаление из базы тестовых курьеров")
    public void cleanUp() {

        courierIdObj = courierClient.getCourierIdObj(courier);

        if (expectedStatusCode == 201) {
            cleanUpCourier();
        }
        if (existingCourier != null) {
            courierClient.delete(courierClient.getCourierIdObj(existingCourier));
        }
    }

}
