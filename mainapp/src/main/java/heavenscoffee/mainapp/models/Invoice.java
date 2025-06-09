package heavenscoffee.mainapp.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "invoice")
public class Invoice {

  @Id
  private String id = UUID.randomUUID().toString();

  @Transient
  private List<OrderItem> orderItems;

  @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "payment_id", referencedColumnName = "id")
  @JsonManagedReference
  private Payment payment;

  @OneToOne
  @JoinColumn(name = "order_id", referencedColumnName = "id")
  @JsonBackReference
  private Order order;

  private LocalDateTime tanggalDibuat;

}
