package orders;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import setup.SetUpTests;

import static org.hamcrest.Matchers.*;

@DisplayName("Принятие заказа")
public class OrdersAcceptTest extends SetUpTests {

    private static final String ACCEPT_404_ORDER = "Заказа с таким id не существует";
    private static final String ACCEPT_404_COURIER = "Курьера с таким id не существует";
    private static final String ACCEPT_400 = "Недостаточно данных для поиска";
    private static final String ACCEPT_409 = "Этот заказ уже в работе";


    @Before
    @Step("Создание тестового курьера и заказа")
    public void setUp() {
        setUpOrder();
        setUpCourier();
    }

    @Test
    @DisplayName("Можно принять заказ, передав id курьера и заказа")
    @Description("Ожидаемый код ответа: 200")
    public void shouldAcceptOrder() {

        expectedStatusCode = 200;

        ordersClient.accept(orderId, courierId)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Если не передать id курьера, запрос вернёт ошибку")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotAcceptOrderWithoutCourierId() {

        expectedStatusCode = 400;

        ordersClient.acceptNoCourierId(orderId)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(ACCEPT_400));

    }

    @Test
    @DisplayName("Если не передать номер заказа, запрос вернёт ошибку")
    @Description("Ожидаемый код ответа: 400")
    public void shouldNotAcceptOrderWithoutOrderId() {

        expectedStatusCode = 400;

        ordersClient.acceptNoOrderId(courierId)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(ACCEPT_400));

    }

    @Test
    @DisplayName("Если передать неверный id курьера, запрос вернёт ошибку")
    @Description("Ожидаемый код ответа: 404")
    public void shouldNotAcceptOrderWithInvalidCourierId() {

        expectedStatusCode = 404;
        courierId = 0;

        ordersClient.accept(orderId, courierId)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(ACCEPT_404_COURIER));

    }

    @Test
    @DisplayName("Если передать неверный номер заказа, запрос вернёт ошибку")
    @Description("Ожидаемый код ответа: 404")
    public void shouldNotAcceptOrderWithInvalidOrderId() {

        expectedStatusCode = 404;
        orderId = 0;

        ordersClient.accept(orderId, courierId)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(ACCEPT_404_ORDER));

    }

    @Test
    @DisplayName("Нельзя принять заказ, который уже находится в работе у курьера")
    @Description("Ожидаемый код ответа: 409")
    public void shouldNotAcceptAcceptedOrder() {

        expectedStatusCode = 409;

        ordersClient.accept(orderId, courierId)
                .statusCode(200);

        ordersClient.accept(orderId, courierId)
                .assertThat()
                .statusCode(expectedStatusCode)
                .and()
                .body("message", is(ACCEPT_409));

    }

    @After
    @Step("Удаление тестовых данных")
    public void cleanUp() {
        ordersClient.finishOrder(orderId);
        cleanUpCourier();
    }
}
