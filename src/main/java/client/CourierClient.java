package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import pojo.courier.Courier;
import pojo.courier.CourierIdObj;

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

    @Step("Получение объекта CourierId курьера")
    public CourierIdObj getCourierIdObj(Courier courier) {
        return login(courier)
                .extract()
                .body()
                .as(CourierIdObj.class);
    }

    @Step("Получение id курьера в виде числа")
    public int getIdNumber(Courier courier) {
        CourierClient client = new CourierClient();
        return Integer.parseInt(client
                .getCourierIdObj(courier)
                .getId());
    }

    @Step("Спек для удаления курьера")
    public RequestSpecification getSpecForDelete(CourierIdObj courierIdObj, boolean withId) {
        if (withId) return getSpec()
                .pathParam("id", Integer.parseInt(courierIdObj.getId()))
                .and()
                .body(courierIdObj);
        else return getSpec()
                .pathParam("id", Integer.parseInt(courierIdObj.getId()));
    }

    @Step("Запрос на удаление курьера по объекту CourierId")
    public ValidatableResponse delete(CourierIdObj courierIdObj) {
        Response response = getSpecForDelete(courierIdObj, true)
                .when()
                .delete(COURIER_ID);
        addToReport(courierIdObj, response);
        return response
                .then()
                .log().all();
    }

        @Step("Запрос на удаление без id в запросе")
    public ValidatableResponse deleteWithoutId(CourierIdObj courierIdObj) {
        Response response = getSpecForDelete(courierIdObj, false)
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

    //убрать точку в 404
    @Step("Проверка сообщения ответа (message)")
    public static String getDeleteMessage(int statusCode) {
        switch (statusCode) {
            case 400:
                return "Недостаточно данных для удаления курьера";
            case 404:
                return "Курьера с таким id нет.";
            default:
                return "Неизвестный код";
        }

    }
}
