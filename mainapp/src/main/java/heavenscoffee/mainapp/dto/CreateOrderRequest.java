package heavenscoffee.mainapp.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {
    private String userId;
    private List<RequestedItem> items; // <-- Ini dia!
    private String alamat;
    private String metodePembayaran;

}
