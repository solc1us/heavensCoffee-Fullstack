package heavenscoffee.mainapp.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import heavenscoffee.mainapp.models.Product;

public interface ProductRepo extends JpaRepository<Product, String> {

  Optional<Product> findByNama(String nama);

}
