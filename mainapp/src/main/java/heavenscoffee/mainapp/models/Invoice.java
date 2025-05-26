package heavenscoffee.mainapp.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
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
  private List<Product> products;

  @OneToOne
  @JoinColumn(name = "payment_id", referencedColumnName = "id")
  private Payment payment;
  
  private Date tanggalDibuat;

  // public Invoice(Product[] products, Payment payment) {
  //   this.products = products;
  //   this.payment = payment;
  //   this.tanggalDibuat = new Date();
  // }

  public void cetakInvoice() {
    System.out.println("ID Invoice: " + id);
    System.out.println("Tanggal Dibuat: " + tanggalDibuat);
    System.out.println("Daftar Produk:");
    for (Product product : products) {
      System.out.println("- " + product.getNama() + ": " + product.getHarga());
    }
    System.out.println("Metode Pembayaran: " + payment.getMetodePembayaran());
    System.out.println("Total Tagihan: " + payment.getTotalTagihan());
  }
}
