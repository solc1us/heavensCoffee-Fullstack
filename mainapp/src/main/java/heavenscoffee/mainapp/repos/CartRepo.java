package heavenscoffee.mainapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import heavenscoffee.mainapp.models.Cart;

public interface CartRepo extends JpaRepository<Cart, String> {

    // Additional query methods can be defined here if needed
    // For example, you might want to find carts by user ID or status
  
}
