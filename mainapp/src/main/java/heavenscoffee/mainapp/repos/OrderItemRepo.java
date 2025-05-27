package heavenscoffee.mainapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import heavenscoffee.mainapp.models.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, String> {

  
}
