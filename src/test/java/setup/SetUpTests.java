package setup;

import client.CourierClient;
import client.OrdersClient;
import io.restassured.response.ValidatableResponse;
import pojo.courier.Courier;
import pojo.courier.CourierIdObj;
import pojo.orders.Order;

import static data.CourierData.*;
import static pojo.courier.Courier.*;
import static pojo.orders.Order.*;

public class SetUpTests {

    protected OrdersClient ordersClient;
    protected Order order;
    protected int orderId;
    protected int track;
    protected CourierClient courierClient;
    protected Courier courier;
    protected CourierIdObj courierIdObj;
    protected int courierId;
    protected int expectedStatusCode;


    public void setUpOrder() {
        ordersClient = new OrdersClient();
        order = createRandomWithoutColor();
        ValidatableResponse response = ordersClient.create(order);
        response.statusCode(201);
        track = ordersClient.getTrack(response);
        orderId = ordersClient.getId(track);
    }


    public void setUpCourier() {
        courierClient = new CourierClient();
        courier = createRandom(testPassword);
        courierClient.register(courier)
                .statusCode(201);
        courierIdObj = courierClient.getCourierIdObj(courier);
        courierId = courierClient.getIdNumber(courier);
    }

    public void cleanUpCourier() {
        courierClient.delete(courierIdObj);
    }

    public void cleanUpOrder() {
        ordersClient.cancelOrder(track);
    }

}
