package heavenscoffee.mainapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import heavenscoffee.mainapp.models.Order;

public interface OrderRepo extends JpaRepository<Order, String> {

    // Additional query methods can be defined here if needed
  
}
