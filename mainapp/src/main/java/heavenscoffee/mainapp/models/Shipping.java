package heavenscoffee.mainapp.models;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "shipping")
public class Shipping {
  
  @Id
  private String id = UUID.randomUUID().toString();
  private String orderId;
  private String alamat;
  private String nomorResi;
  private String statusPengiriman;
  private Date tanggalKirim;

  public Shipping(String orderId, String alamat) {
    this.orderId = orderId;
    this.alamat = alamat;
    this.nomorResi = UUID.randomUUID().toString();
    this.statusPengiriman = "Dalam Proses";
    this.tanggalKirim = new Date();
  }

  public void updateStatusPengiriman(String statusPengiriman) {
    this.statusPengiriman = statusPengiriman;
  }

}
