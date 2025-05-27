package heavenscoffee.mainapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import heavenscoffee.mainapp.models.Invoice;

public interface InvoiceRepo extends JpaRepository<Invoice, String> {

    // Additional query methods specific to Invoice can be defined here if needed
  
}
