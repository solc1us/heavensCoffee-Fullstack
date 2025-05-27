package heavenscoffee.mainapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import heavenscoffee.mainapp.models.Shipping;

public interface ShippingRepo extends JpaRepository<Shipping, String> {

    // Additional query methods specific to Shipping can be defined here if needed
    // For example, you might want to find shipments by status or tracking number
  
}
