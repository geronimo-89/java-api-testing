package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import pojo.courier.Courier;
import pojo.courier.CourierId;

import static pojo.courier.Courier.loginPasswordFrom;

public class CourierClient extends ScooterBaseClient {

    @Step("Запрос на регистрацию нового курьера")
    public ValidatableResponse registerNew(Courier courier) {
        Response response = getSpec()
                .and()
                .body(courier)
                .when()
                .post(COURIER_ROOT);
        addToReport(courier, response);
        return response
                .then()
                .log().all();
    }

    @Step("Запрос на логин курьера")
    public ValidatableResponse login(Courier courier) {
        Response response = getSpec()
                .and()
                .body(loginPasswordFrom(courier))
                .when()
                .post(COURIER_LOGIN);
        addToReport(courier, response);
        return response
                .then()
                .log().all();
    }

    @Step("Получение POJO с ID курьера")
    public CourierId getCourierId(Courier courier) {
        return login(courier)
                .extract()
                .body()
                .as(CourierId.class);
    }

    @Step("Запрос на удаление курьера")
    public ValidatableResponse deleteCourier(Courier courier) {
        CourierId courierId = getCourierId(courier);
        int id;
        try {
            id = Integer.parseInt(courierId.getId());
        } catch (NumberFormatException e) {
            id = 0;
        }
        Response response = getSpec()
                .pathParam("id", id)
                .and()
                .body(courierId)
                .when()
                .delete(COURIER_ID);
        addToReport(courierId, response);
        return response
                .then()
                .log().all();
    }

    @Step("Проверка сообщения ответа (message)")
    public static String getRegisterMessage(int statusCode) {
        switch (statusCode) {
            case 400:
                return "Недостаточно данных для создания учетной записи";
            case 409:
                return "Этот логин уже используется. Попробуйте другой.";
            default:
                return "Неизвестный код";
        }
    }

    @Step("Проверка сообщения ответа (message)")
    public static String getLoginMessage(int statusCode) {
        switch (statusCode) {
            case 400:
                return "Недостаточно данных для входа";
            case 404:
                return "Учетная запись не найдена";
            default:
                return "Неизвестный код";
        }
    }

}
