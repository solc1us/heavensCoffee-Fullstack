package heavenscoffee.mainapp.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import heavenscoffee.mainapp.models.User;
import heavenscoffee.mainapp.repos.CartRepo;
import heavenscoffee.mainapp.repos.UserRepo;
import heavenscoffee.mainapp.utils.MessageModel;
import heavenscoffee.mainapp.utils.MessageModelPagination;
import heavenscoffee.mainapp.utils.SortingAndAscendingDescending;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  CartRepo cartRepo;

  @Autowired
  UserRepo userRepo;

  @Autowired
  SortingAndAscendingDescending sortingAndAscendingDescending;

  @PostMapping("/create")
  public ResponseEntity<Object> createUser(@RequestBody User data) {

    MessageModel msg = new MessageModel();

    try {
      User user = new User(); 

      user.setUsername(data.getUsername());
      user.setPassword(data.getPassword());
      user.setEmail(data.getEmail());
      user.setNomorHp(data.getNomorHp());
      user.setRole(data.getRole());
      
      userRepo.save(user);

      msg.setMessage("User created successfully");
      msg.setData(user);

      return ResponseEntity.status(HttpStatus.OK).body(msg);

    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
    }
  }

  @GetMapping("/findall")
  public ResponseEntity<MessageModelPagination> getDataPagination(
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "urutan", required = false) String urutan) {

    MessageModelPagination msg = new MessageModelPagination();
    
    try {
      Sort objSort = sortingAndAscendingDescending.getSortingData(sort, urutan);
      Pageable pageRequest = objSort == null ? PageRequest.of(page, size) : PageRequest.of(page, size, objSort);

      Page<User> data = userRepo.findAll(pageRequest);

      msg.setMessage("Success");
      msg.setData(data.getContent());
      msg.setTotalPages(data.getTotalPages());
      msg.setCurrentPage(data.getNumber());
      msg.setTotalItems((int) data.getTotalElements());
      msg.setNumberOfElement(data.getNumberOfElements());

      return ResponseEntity.status(HttpStatus.OK).body(msg);
    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
    }
  }

  @PutMapping("/update")
  public ResponseEntity<Object> update(@RequestBody User param) {

    MessageModel msg = new MessageModel();

    try {

      if (param.getId().isEmpty() || param.getId().trim().length() < 1) {
        return ResponseEntity.badRequest().body("ID is required in the request body.");
      }

      Optional<User> existingUser = userRepo.findById(param.getId());

      if (existingUser.isPresent()) {
        User userToUpdate = existingUser.get();

        userToUpdate.setUsername(param.getUsername());
        userToUpdate.setPassword(param.getPassword());
        userToUpdate.setEmail(param.getEmail());
        userToUpdate.setNomorHp(param.getNomorHp());
        userToUpdate.setRole(param.getRole());
        userRepo.save(userToUpdate);

        msg.setMessage("Sukses");
        msg.setData(userToUpdate);

      } else {
        msg.setMessage("User dengan ID " + param.getId() + " tidak ditemukan.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      return ResponseEntity.status(HttpStatus.OK).body(msg);

    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }
  }

  @DeleteMapping("/deletebyid/{id}")
  public ResponseEntity<Object> deleteById(@PathVariable String id) {
    MessageModel msg = new MessageModel();
    try {
      Optional<User> user = userRepo.findById(id);
      if (user.isEmpty()) {
        msg.setMessage("User tidak ditemukan");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }
      userRepo.delete(user.get());
      cartRepo.findById(user.get().getCartId()).ifPresent(cart -> cartRepo.delete(cart));
      msg.setMessage("User berhasil dihapus");
      msg.setData(user.get());
      return ResponseEntity.status(HttpStatus.OK).body(msg);
    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }
  }

  @DeleteMapping("/deletebatch")
  public ResponseEntity<Object> deleteItems(@RequestBody List<User> id) {
    userRepo.deleteAll(id);
    for (int i = 0; i < id.size(); i++) {
      String userId = id.get(i).getId();
      cartRepo.findById(userId).ifPresent(cart -> cartRepo.delete(cart));
    }
    return ResponseEntity.status(HttpStatus.OK).body("Semua item berhasil dihapus");
  }
  
  

}
