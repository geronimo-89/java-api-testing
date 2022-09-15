package orders;

import client.OrdersClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.Matchers.*;

@DisplayName("Список заказов")
public class OrdersGetTest {

    OrdersClient client;
    int expectedStatusCode;

    @Before
    @Step("Создание клиента")
    public void setUp() {
        client = new OrdersClient();
    }

    @Test
    @DisplayName("Можно получить не пустой список заказов")
    @Description("Ожидаемый код ответа: 200")
    public void shouldGetListOfOrders() {

        expectedStatusCode = 200;
        client.getOrders()
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("orders", hasSize(greaterThan(0)));

    }

        @Test
    @DisplayName("Можно получить не пустой список заказов на случайной странице со случайным лимитом (из доступных)")
    @Description("Ожидаемый код ответа: 200")
    public void shouldGetListOfOrdersWithPageLimit() {

        expectedStatusCode = 200;
        client.getRandomPageLimitOrders()
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("orders", hasSize(greaterThan(0)));

    }

}
