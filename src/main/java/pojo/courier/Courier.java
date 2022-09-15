package pojo.courier;

import io.qameta.allure.Step;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;

public class Courier {

    @Getter
    @Setter
    private String login;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String firstName;

    public Courier() {
    }

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public void setRandomLogin(int length) {
        this.login = RandomStringUtils.randomAlphanumeric(length);
    }

    public void setRandomPassword(int length) {
        this.password = RandomStringUtils.randomAlphanumeric(length);
    }

    @Step("Получение объекта курьера со случайным логином, с паролем и именем")
    public static Courier createRandom(String password, String firstName) {
        Courier courier = new Courier();
        courier.setRandomLogin(12);
        courier.setPassword(password);
        courier.setFirstName(firstName);
        return courier;
    }

    @Step("Получение объекта курьера с логином, который уже зарегистрирован")
    public static Courier createWithExistingLogin(Courier existingCourier) {
        Courier newCourier = new Courier();
        newCourier.setLogin(existingCourier.getLogin());
        newCourier.setPassword(existingCourier.getPassword());
        newCourier.setFirstName(existingCourier.getFirstName());
        return newCourier;
    }

    @Step("Получение объекта курьера с логином и паролем")
    public static Courier loginPasswordFrom(Courier courier) {
        return new Courier(courier.getLogin(), courier.getPassword(), null);
    }

    @Step("Получение объекта курьера только с логином")
    public static Courier loginFrom(Courier courier) {
        return new Courier(courier.getLogin(), null, null);
    }

    @Step("Получение объекта курьера только с паролем")
    public static Courier passwordFrom(Courier courier) {
        return new Courier(null, courier.getPassword(), null);
    }

    @Step("Получение объекта курьера без логина")
    public static Courier withoutLoginFrom(Courier courier) {
        return new Courier(null, courier.getPassword(), courier.getFirstName());
    }

    @Step("Получение объекта курьера без пароля")
    public static Courier withoutPasswordFrom(Courier courier) {
        return new Courier(courier.getLogin(), null, courier.getFirstName());
    }

}
