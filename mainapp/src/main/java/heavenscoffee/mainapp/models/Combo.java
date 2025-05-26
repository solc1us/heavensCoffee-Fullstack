package heavenscoffee.mainapp.models;

import jakarta.persistence.Entity;
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
// @NoArgsConstructor
// @AllArgsConstructor
// @Table(name = "combo")
public class Combo extends Product {

    

    // public Combo(String nama, String deskripsi, int harga, int stok) {
    //     super(nama, deskripsi, harga, stok);
    // }

    // public void setStok(int stok) {
    //     super.setStok(stok);
    // }
  
}
