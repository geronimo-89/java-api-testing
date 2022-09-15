package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import pojo.courier.Courier;
import pojo.courier.CourierId;

import static pojo.courier.Courier.loginPasswordFrom;

public class CourierClient extends ScooterBaseClient {

    @Step("Запрос на регистрацию нового курьера")
    public ValidatableResponse register(Courier courier) {
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

    @Step("Получение CourierId курьера")
    public CourierId getCourierId(Courier courier) {
        return login(courier)
                .extract()
                .body()
                .as(CourierId.class);
    }

    public RequestSpecification getSpecForDelete(CourierId courierId, boolean withId) {
        if (withId) return getSpec()
                .pathParam("id", Integer.parseInt(courierId.getId()))
                .and()
                .body(courierId);
        else return getSpec()
                .pathParam("id", Integer.parseInt(courierId.getId()));
    }

    @Step("Запрос на удаление курьера по объекту CourierId")
    public ValidatableResponse delete(CourierId courierId) {
        Response response = getSpecForDelete(courierId, true)
                .when()
                .delete(COURIER_ID);
        addToReport(courierId, response);
        return response
                .then()
                .log().all();
    }

        @Step("Запрос на удаление без id в запросе")
    public ValidatableResponse deleteWithoutId(CourierId courierId) {
        Response response = getSpecForDelete(courierId, false)
                .when()
                .delete(COURIER_ID);
        addToReport(response);
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

    @Step("Проверка сообщения ответа (message)")
    public static String getDeleteMessage(int statusCode) {
        switch (statusCode) {
            case 400:
                return "Недостаточно данных для удаления курьера";
            case 404:
                return "Курьера с таким id нет";
            default:
                return "Неизвестный код";
        }

    }
}
