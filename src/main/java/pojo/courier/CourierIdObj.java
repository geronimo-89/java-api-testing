package pojo.courier;

import lombok.Getter;
import lombok.Setter;

public class CourierIdObj {

    @Getter
    @Setter
    private String id;

    public CourierIdObj(String id) {
        this.id = id;
    }

    public CourierIdObj() {
    }
}
