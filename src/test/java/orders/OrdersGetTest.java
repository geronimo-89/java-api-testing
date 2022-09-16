package orders;

import client.OrdersClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import setup.SetUpTests;

import static org.hamcrest.Matchers.*;

@DisplayName("Список заказов")
public class OrdersGetTest extends SetUpTests {

    @Before
    @Step("Создание клиента и заказа в случае, если база пустая")
    public void setUp() {
        ordersClient = new OrdersClient();
        if (ordersClient.getTotalOrdersNumber() == 0)
            setUpOrder();
    }

    @Test
    @DisplayName("Можно получить не пустой список заказов")
    @Description("Ожидаемый код ответа: 200")
    public void shouldGetListOfOrders() {

        expectedStatusCode = 200;
        ordersClient.getAllOrders()
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
        ordersClient.getRandomPageLimitOrders()
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("orders", hasSize(greaterThan(0)));

    }

    @After
    @DisplayName("Удаление тестового заказа")
    public void cleanUp() {
        if (order != null)
            cleanUpOrder();
    }

}
