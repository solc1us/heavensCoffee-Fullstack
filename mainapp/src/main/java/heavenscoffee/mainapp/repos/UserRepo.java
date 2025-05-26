package heavenscoffee.mainapp.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import heavenscoffee.mainapp.models.User;

public interface UserRepo extends JpaRepository<User, String> {
    
}
