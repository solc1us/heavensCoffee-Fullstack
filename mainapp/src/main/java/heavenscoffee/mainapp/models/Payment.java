
package heavenscoffee.mainapp.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "payment")
public class Payment {

  @Id
  private String id = UUID.randomUUID().toString();

  @OneToOne
  @JoinColumn(name = "order_id", referencedColumnName = "id")
  @JsonBackReference
  private Order order;

  @OneToOne
  @JoinColumn(name = "invoice_id", referencedColumnName = "id")
  @JsonBackReference
  private Invoice invoice;

  private String metodePembayaran;
  private int totalTagihan;
  private LocalDateTime tanggalPembayaran;
  private String statusPembayaran;

}
