package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import pojo.courier.Courier;
import pojo.courier.CourierIdObj;

import static pojo.courier.Courier.loginPasswordFrom;

public class CourierClient extends BaseClient {

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

    @Step("Запрос на удаление курьера по объекту CourierId")
    public ValidatableResponse delete(CourierIdObj courierIdObj) {
        int id = Integer.parseInt(courierIdObj.getId());
        Response response = getSpec()
                .pathParam("id", id)
                .and()
                .body(courierIdObj)
                .when()
                .delete(COURIER_ID);
        addToReport(courierIdObj, response);
        return response
                .then()
                .log().all();
    }

        @Step("Запрос на удаление без id в теле запроса")
    public ValidatableResponse deleteWithoutBodyId(CourierIdObj courierIdObj) {
        int id = Integer.parseInt(courierIdObj.getId());
        Response response = getSpec()
                .pathParam("id", id)
                .when()
                .delete(COURIER_ID);
        addToReport(response);
        return response
                .then()
                .log().all();
    }

    @Step("Запрос на удаление без id в пути запроса")
        public ValidatableResponse deleteWithoutPathId(CourierIdObj courierIdObj) {
        int id = Integer.parseInt(courierIdObj.getId());
        Response response = getSpec()
                .body(courierIdObj)
                .when()
                .delete(COURIER_ROOT);
        addToReport(courierIdObj, response);
        return response
                .then()
                .log().all();
    }

}
