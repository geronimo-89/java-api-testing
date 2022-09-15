package orders;

import client.OrdersClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.orders.Order;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static client.OrdersClient.*;
import static java.util.Arrays.*;
import static pojo.orders.Order.*;

@RunWith(Parameterized.class)
@DisplayName("Создание заказа")
public class OrderPlaceTest {

    private final List<String> colors;
    Order order;
    OrdersClient client;
    int expectedStatusCode;
    int orderId;

    public OrderPlaceTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Color: {0}")
    public static Object[] generateData() {
        return new Object[]{
                asList(GREY),
                asList(BLACK),
                asList(BLACK, GREY),
                asList(PURPLE),
                asList(""),
                null,
        };
    }

    @Test
    @DisplayName("Можно создать заказ с любым набором цветов")
    @Description("Ожидаемый код ответа: 201")
    public void shouldPlaceOrderWithAnyColors() {

        expectedStatusCode = 201;

        client = new OrdersClient();
        order = createRandomWithoutColor();
        order.setColor(colors);
        ValidatableResponse response = client.placeOrder(order);
        response
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("track", is(notNullValue()));

        orderId = client.getOrderId(response);
    }

    @After
    @DisplayName("Удаление тестового заказа")
    public void cleanUp() {
        client.deleteOrder(orderId)
                .statusCode(200);
    }

}


