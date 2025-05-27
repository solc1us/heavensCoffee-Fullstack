package heavenscoffee.mainapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import heavenscoffee.mainapp.models.Payment;

public interface PaymentRepo extends JpaRepository<Payment, String> {

    // Additional query methods can be defined here if needed
    // For example, you might want to find payments by user or status
    // Optional<Payment> findByUserId(String userId);
    // List<Payment> findByStatus(String status);
  
}
