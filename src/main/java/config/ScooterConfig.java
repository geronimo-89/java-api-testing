package config;

/*
http://qa-scooter.praktikum-services.ru/docs/
 */

public class ScooterConfig {

    protected static final String BASE_URL = "http://qa-scooter.praktikum-services.ru/api/v1";
    protected static final String COURIER_ROOT = "/courier";
    protected static final String COURIER_LOGIN = COURIER_ROOT + "/login";
    protected static final String COURIER_ID = COURIER_ROOT + "/{id}";
    protected static final String ORDERS_ROOT = "/orders";
    protected static final String ORDER_CANCEL = ORDERS_ROOT + "/cancel";
    protected static final String ORDER_FINISH = ORDERS_ROOT + "/finish/{id}";
    protected static final String ORDER_TRACK = ORDERS_ROOT + "/track";
    protected static final String ORDER_ACCEPT = ORDERS_ROOT + "/accept";

    protected static final String ORDER_ACCEPT_ORDER =  ORDER_ACCEPT + "/{orderId}";

}
