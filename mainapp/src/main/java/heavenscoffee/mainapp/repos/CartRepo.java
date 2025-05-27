package heavenscoffee.mainapp.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import heavenscoffee.mainapp.models.Cart;

public interface CartRepo extends JpaRepository<Cart, String> {

    Optional<Cart> findByUserId(String userId);
  
}
