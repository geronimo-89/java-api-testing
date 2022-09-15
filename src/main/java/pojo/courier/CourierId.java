package pojo.courier;

import lombok.Getter;
import lombok.Setter;

public class CourierId {

    @Getter
    @Setter
    private String id;

    public CourierId(String id) {
        this.id = id;
    }

    public CourierId() {
    }
}
