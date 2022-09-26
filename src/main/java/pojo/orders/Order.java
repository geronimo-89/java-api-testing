package pojo.orders;

import io.qameta.allure.Step;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static data.DataGenerator.*;

public class Order {

    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    @Getter
    @Setter
    private String address;
    @Getter
    @Setter
    private Integer metroStation;
    @Getter
    @Setter
    private String phone;
    @Getter
    @Setter
    private Integer rentTime;
    @Getter
    @Setter
    private String deliveryDate;
    @Getter
    @Setter
    private String comment;
    @Getter
    @Setter
    private List<String> color;

    public Order() {
    }

    public Order(String firstName, String lastName, String address, Integer metroStation, String phone, Integer rentTime, String deliveryDate, String comment, List<String> color) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    public static String randomFirstName() {
        return randomCyrillic(2, 15, false);
    }

    public static String randomLastName() {
        return randomCyrillic(2, 30, false);
    }

    public static String randomAddress() {
        return randomCyrillic(5, 49, true);
    }

    public static Integer randomMetroStation() {
        return randomBetween(1, 237);
    }

    public static String randomPhone() {
        return randomNumeric(11, 13, false);
    }

    public static Integer randomRentTime() {
        return randomBetween(1, 7);
    }

    public static String randomDeliveryDate() {
        return randomDateFromNow(2023, 12, 31);
    }

    public static String randomComment() {
        return randomCyrillic(5, 50, true);
    }

    @Step("Создание заказа со случайным набором данных, без цвета")
    public static Order createRandomWithoutColor() {
        return new Order(
                randomFirstName(),
                randomLastName(),
                randomAddress(),
                randomMetroStation(),
                randomPhone(),
                randomRentTime(),
                randomDeliveryDate(),
                randomComment(),
                null);
    }


}
