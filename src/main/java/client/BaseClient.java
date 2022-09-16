package client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.ScooterConfig;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BaseClient extends ScooterConfig {

    static Gson gson;

    @Step("Передача базовых спецификаций запроса")
    protected RequestSpecification getSpec() {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(BASE_URL);
    }

    @Step("Добавление запроса и ответа в отчет Allure")
    protected static void addToReport(Object object, Response response) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        Allure.addAttachment("Запрос", gson.toJson(object));
        Allure.addAttachment("Ответ", response.prettyPrint());
    }

    @Step("Добавление ответа в отчет Allure")
    protected static void addToReport(Response response) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        Allure.addAttachment("Ответ", response.prettyPrint());
    }

}
