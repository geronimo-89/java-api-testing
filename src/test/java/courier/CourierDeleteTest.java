package courier;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import setup.SetUpTests;

import static client.CourierClient.getDeleteMessage;
import static client.CourierClient.getOk;
import static org.hamcrest.Matchers.is;

@DisplayName("Удаление курьера")
public class CourierDeleteTest extends SetUpTests {

    @Before
    @Step("Создание курьера и клиента для запросов с ним")
    public void setUp() {
        setUpCourier();
    }

    @Test
    @DisplayName("Существующего в базе курьера можно удалить")
    @Description("Ожидаемый код ответа: 200")
    public void shouldDeleteExistingCourier() {

        expectedStatusCode = 200;

        courierClient.delete(courierIdObj)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("ok", is(getOk(expectedStatusCode)));
    }

    @Test
    @DisplayName("Несуществующего (уже удаленного из базы) курьера нельзя удалить")
    @Description("Ожидаемый код ответа: 404")
    public void shouldNotDeleteDeletedCourier() {

        expectedStatusCode = 404;

        courierClient.delete(courierIdObj)
                .statusCode(200);

        courierClient.delete(courierIdObj)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getDeleteMessage(expectedStatusCode)));
    }

    @Test
    @DisplayName("Если отправить запрос на удаление курьера без id, вернётся ошибка")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotDeleteWithoutId() {

        expectedStatusCode = 400;

        courierClient.deleteWithoutId(courierIdObj)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getDeleteMessage(expectedStatusCode)));
    }

    @After
    @Step("Удаление из базы тестовых курьеров")
    public void cleanUp() {
            cleanUpCourier();
    }

}
