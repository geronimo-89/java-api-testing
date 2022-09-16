package orders;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import setup.SetUpTests;

import static client.ScooterBaseClient.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Принятие заказа")
public class OrdersAcceptTest extends SetUpTests {


    @Before
    @Step("Создание тестового курьера и заказа")
    public void setUp() {
        setUpOrder();
        setUpCourier();
    }

    @Test
    @DisplayName("Можно принять заказ, передав id курьера и заказа")
    @Description("Ожидаемый код ответа...")
    public void shouldAcceptOrder() {

        expectedStatusCode = 200;

        ordersClient.accept(orderId, courierId)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("ok", is(getOk(expectedStatusCode)));
    }
    
    @After
    @Step("Удаление тестовых данных")
    public void cleanUp() {
        ordersClient.finishOrder(orderId);
        cleanUpCourier();
    }
}
