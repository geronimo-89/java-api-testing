package orders;

import client.OrdersClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.orders.Order;

import static client.OrdersClient.*;
import static pojo.orders.Order.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Поиск заказа")
public class OrdersFindTest {

    Order order;
    OrdersClient client;
    int expectedStatusCode;
    int track;

    @Before
    @Step("Создание тестового заказа")
    public void setUp() {
        client = new OrdersClient();
        order = createRandomWithoutColor();
        ValidatableResponse response = client.create(order);
        response.statusCode(201);
        track = client.getId(response);

    }

    @Test
    @DisplayName("Можно найти нужный заказ по трекинговому номеру")
    @Description("Ожидаемый код ответа: 200")
    public void shouldFindOrderWithId() {

        expectedStatusCode = 200;

        client.getOrder(track)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("order.track", is(track));
    }

    @Test
    @DisplayName("Запрос без номера заказа возвращает ошибку")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotFindOrderWithoutId() {

        expectedStatusCode = 400;

        client.getOrder()
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getFindMessage(expectedStatusCode)));

    }

    @Test
    @DisplayName("Запрос с несуществующим заказом возвращает ошибку")
    @Description("Ожидаемый код ответа: 404")
    public void shouldNotFindNotExistingOrder() {

        expectedStatusCode = 404;

        client.cancelOrder(track);

        client.getOrder(track)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(getFindMessage(expectedStatusCode)));

    }

    @After
    @DisplayName("Удаление тестовых заказов")
    public void cleanUp() {
        if (expectedStatusCode != 404)
            client.cancelOrder(track)
                    .statusCode(200);
    }
}
