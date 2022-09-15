package pojo.orders;

import lombok.Getter;
import lombok.Setter;

public class OrderId {

    @Getter
    @Setter
    private int id;

    public OrderId(int id) {
        this.id = id;
    }

    public OrderId() {
    }
}
