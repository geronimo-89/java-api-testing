package client;

import data.DataGenerator;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import pojo.orders.Order;

import static data.DataGenerator.*;
import static io.restassured.specification.SpecificationQuerier.*;

public class OrdersClient extends ScooterBaseClient {

    public static final String BLACK = "BLACK";
    public static final String GREY = "GREY";
    public static final String PURPLE = "PURPLE";

    @Step("Запрос на создание заказа")
    public ValidatableResponse placeOrder(Order order) {
        Response response = getSpec()
                .and()
                .body(order)
                .when()
                .post(ORDERS_ROOT);
        addToReport(order, response);
        return response
                .then()
                .log().all();
    }

    @Step("Получение ID заказа")
    public int getOrderId(ValidatableResponse response) {
        return response
                .extract()
                .path("track");
    }

    @Step("Запрос на удаление (отмену) заказа")
    public ValidatableResponse deleteOrder(int id) {
        Response response = getSpec()
                .pathParam("id", id)
                .when()
                .put(ORDER_CANCEL);
        addToReport(id, response);
        return response
                .then()
                .log().all();

    }

    @Step("Запрос на общее количество заказов")
    public int getTotalOrdersNumber() {
        return getSpec()
                .when()
                .get(ORDERS_ROOT)
                .then()
                .extract()
                .path("pageInfo.total");
    }

    @Step("Запрос на получение списка заказов")
    public ValidatableResponse getOrders() {
        Response response = getSpec()
                .when()
                .get(ORDERS_ROOT);
        addToReport(response);
        return response
                .then()
                .log().all();
    }

    @Step("Запрос на получение списка заказов на случайной странице со случайным лимитом")
    public ValidatableResponse getRandomPageLimitOrders() {
        OrdersClient client = new OrdersClient();
        int ordersLimit = randomBetween(1, 30);
        int maxPage = client.getTotalOrdersNumber() / ordersLimit;
        int pageLimit = randomBetween(1, maxPage);

        RequestSpecification spec = getSpec()
                .queryParam("limit", ordersLimit)
                .queryParam("page", pageLimit);

        Response response = spec
                .when()
                .get(ORDERS_ROOT);

        addToReport(query(spec).getQueryParams(), response);

        return response
                .then()
                .log().all();
    }

}

