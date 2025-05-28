package heavenscoffee.mainapp.models;

import java.time.LocalDateTime;
import java.util.Date;
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

  // public Invoice(Product[] products, Payment payment) {
  // this.products = products;
  // this.payment = payment;
  // this.tanggalDibuat = new Date();
  // }

  // public void cetakInvoice() {
  //   System.out.println("ID Invoice: " + id);
  //   System.out.println("Tanggal Dibuat: " + tanggalDibuat);
  //   System.out.println("Daftar Produk:");
  //   for (OrderItem item : orderItems) {
  //     System.out.printf("%-24s %-15s %-15s %-40s%n", item.getProduct().getNama(), item.getKuantitas(),
  //         item.getHargaSatuan(), item.getTotalHarga());
  //   }
  //   System.out.println("Metode Pembayaran: " + payment.getMetodePembayaran());
  //   System.out.println("Total Tagihan: " + payment.getTotalTagihan());
  // }
}
