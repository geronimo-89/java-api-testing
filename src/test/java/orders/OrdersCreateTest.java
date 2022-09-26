package orders;

import client.OrdersClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import setup.SetUpTests;

import java.util.List;

import static client.OrdersClient.*;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static pojo.orders.Order.createRandomWithoutColor;

@RunWith(Parameterized.class)
@DisplayName("Создание заказа")
public class OrdersCreateTest extends SetUpTests {

    private final List<String> colors;

    public OrdersCreateTest(List<String> colors) {
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

        ordersClient = new OrdersClient();
        order = createRandomWithoutColor();
        order.setColor(colors);
        ValidatableResponse response = ordersClient.create(order);
        response
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("track", is(notNullValue()));

        track = ordersClient.getTrack(response);
    }

    @After
    @DisplayName("Удаление тестового заказа")
    public void cleanUp() {
        cleanUpOrder();
    }

}
