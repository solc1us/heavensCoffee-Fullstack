
package heavenscoffee.mainapp.models;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart {
    
    @Id
    private String id = UUID.randomUUID().toString();
    private String userId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItem;

    public Cart(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setOrderItem(OrderItem orderItem) {
        if (this.orderItem == null) {
            this.orderItem = new ArrayList<>();
        }
        this.orderItem.add(orderItem);
    }

    public void showCart() {

        System.out.println("=========================");
        System.out.println("Order Items: ");

        if (orderItem != null) {
            System.out.println("Nama \t\t\t Kuantitas \t Harga Satuan \t Total Harga");
            for (OrderItem item : orderItem) {
                item.showOrderItem();

            }
        } else {
            System.out.println("Tidak ada barang di cart");
        }
    }
}
