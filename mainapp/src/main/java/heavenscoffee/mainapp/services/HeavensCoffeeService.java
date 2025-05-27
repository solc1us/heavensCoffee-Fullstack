package heavenscoffee.mainapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import heavenscoffee.mainapp.models.Cart;
import heavenscoffee.mainapp.models.User;
import heavenscoffee.mainapp.repos.CartRepo;
import heavenscoffee.mainapp.utils.MessageModel;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class HeavensCoffeeService {

  @Autowired
  CartRepo cartRepo;

  public String initializeCart(User user) {
    try {
      Cart cart = new Cart();

      cart.setUserId(user.getId());

      cartRepo.save(cart);

      return cart.getId();

    } catch (Exception e) {
      return e.getMessage();
    }
  }

}
