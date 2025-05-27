package heavenscoffee.mainapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemInCart {
  private String userId;
  private String productId;
  private int quantity;

}
