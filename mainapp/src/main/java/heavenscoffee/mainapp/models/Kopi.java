package heavenscoffee.mainapp.models;

import jakarta.persistence.Entity;
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
// @Table(name = "shipping")
public class Kopi extends Product {
  private String kategori;

  public Kopi(String nama, String deskripsi, int harga, int stok, String kategori) {
    super(nama, deskripsi, harga, stok);
    this.kategori = kategori;
  }

  public void setKategori(String kategori) {
    this.kategori = kategori;
  }

  public void setStok(int stok) {
    super.setStok(stok);
  }

}
