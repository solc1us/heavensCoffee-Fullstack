package heavenscoffee.mainapp.models;

import java.util.ArrayList;
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
@Table(name = "product")
public abstract class Product {

  @Id
  private String id = UUID.randomUUID().toString();
  private String nama;
  private String deskripsi;
  private int harga;
  private int stok;

  public Product(String nama, String deskripsi, int harga, int stok) {
    this.nama = nama;
    this.deskripsi = deskripsi;
    this.harga = harga;
    this.stok = stok;
  }

  public void showProduct() {
    // System.out.println(nama + " \t " + deskripsi + " \t " + harga + " \t " + stok);
    System.out.printf("%-23s %-39s %-15s %-40s%n", nama, deskripsi, harga, stok);
  }
  
  public String getNama() {
    return nama;
  }

  public int getHarga() {
    return harga;
  };

  public int getStok() {
    return stok;
  }

  public void setStok(int stok) {
    this.stok = stok;
  }

}
