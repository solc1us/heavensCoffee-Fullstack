package heavenscoffee.mainapp.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import heavenscoffee.mainapp.models.Product;

public interface ProductRepo extends JpaRepository<Product, String> {

  @Query("SELECT p FROM Product p WHERE TYPE(p) = ?1")
  List<Product> findAllByClassName(String className);

}
