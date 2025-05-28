package heavenscoffee.mainapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {
    private String userId;
    private String alamat;
    private String metodePembayaran;

}
