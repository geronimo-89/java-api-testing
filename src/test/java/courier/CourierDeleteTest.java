package courier;

import client.CourierClient;
import data.CourierData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import pojo.courier.Courier;
import pojo.courier.CourierId;

import static client.CourierClient.*;
import static pojo.courier.Courier.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Удаление курьера")
public class CourierDeleteTest extends CourierData {

    Courier courier;
    CourierClient client;
    int expectedStatusCode;
    CourierId courierId;


    @Before
    @Step("Создание курьера и клиента для запросов с ним")
    public void setUp() {

        client = new CourierClient();
        courier = createRandom(testPassword);
        client.register(courier)
                .statusCode(201);
        courierId = client.getCourierId(courier);
    }

    @Test
    @DisplayName("Существующего в базе курьера можно удалить")
    @Description("Ожидаемый код ответа: 200")
    public void shouldDeleteExistingCourier() {

        expectedStatusCode = 200;

        client.delete(courierId)
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

        client.delete(courierId)
                .statusCode(200);

        client.delete(courierId)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getDeleteMessage(expectedStatusCode)));
    }

    @Test
    @DisplayName("Eсли отправить запрос на удаление курьера без id, вернётся ошибка")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotDeleteWithoutId() {

        expectedStatusCode = 400;

        client.deleteWithoutId(courierId)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getDeleteMessage(expectedStatusCode)));
    }
}
