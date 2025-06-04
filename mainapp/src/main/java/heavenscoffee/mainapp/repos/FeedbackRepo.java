package heavenscoffee.mainapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import heavenscoffee.mainapp.models.Feedback;

public interface FeedbackRepo extends JpaRepository<Feedback, String> {
    
    // Additional query methods can be defined here if needed

  
}
