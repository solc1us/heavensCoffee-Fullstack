package heavenscoffee.mainapp.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import heavenscoffee.mainapp.dto.ItemInCart;
import heavenscoffee.mainapp.models.Cart;
import heavenscoffee.mainapp.models.OrderItem;
import heavenscoffee.mainapp.models.Product;
import heavenscoffee.mainapp.repos.CartRepo;
import heavenscoffee.mainapp.repos.ProductRepo;
import heavenscoffee.mainapp.utils.MessageModel;
import heavenscoffee.mainapp.utils.MessageModelPagination;
import heavenscoffee.mainapp.utils.SortingAndAscendingDescending;

@RestController
@RequestMapping("/cart")
public class CartController {

  @Autowired
  SortingAndAscendingDescending sortingAndAscendingDescending;

  @Autowired
  CartRepo cartRepo;

  @Autowired
  ProductRepo productRepo;

  @GetMapping("/findall")
  public ResponseEntity<MessageModelPagination> findAllOrderPagination(
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "urutan", required = false) String urutan) {
    MessageModelPagination msg = new MessageModelPagination();
    try {
      Sort objSort = sortingAndAscendingDescending.getSortingData(sort, urutan);
      Pageable pageRequest = objSort == null ? PageRequest.of(page, size) : PageRequest.of(page, size, objSort);

      Page<Cart> data = cartRepo.findAll(pageRequest);

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

  @GetMapping("/findbyuserid/{userid}")
  public ResponseEntity<Object> findOrderById(
      @PathVariable("userid") String userId) {

    MessageModel msg = new MessageModel();

    try {

      if (userId.isEmpty() || userId == null) {
        msg.setMessage("'userId' is required in the request param.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Optional<Cart> cart = cartRepo.findByUserId(userId);

      if (!cart.isPresent() || cart == null) {
        msg.setMessage("'user id' yang anda masukkan salah (tidak ada di database).");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      msg.setMessage("Sukses");
      msg.setData(cart);
      return ResponseEntity.ok(msg);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }


  @PostMapping("/additem")
  public ResponseEntity<Object> addItemToCart(@RequestBody ItemInCart addItemToCart) {

    MessageModel msg = new MessageModel();

    try {
      String userId = addItemToCart.getUserId();
      String productId = addItemToCart.getProductId();
      int quantity = addItemToCart.getQuantity();

      Optional<Cart> optCart = cartRepo.findByUserId(userId);
      if (optCart.isEmpty()) {
        msg.setMessage("Cart not found for user: " + userId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Cart cart = optCart.get();

      Optional<Product> optProduct = productRepo.findById(productId);

      if (optProduct.isEmpty()) {
        msg.setMessage("Product tidak ditemukan");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Product product = optProduct.get();

      if (product.getStok() < quantity) {
        msg.setMessage("Stok tidak cukup untuk produk: " + product.getNama());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      // Check if the product already exists in the cart
      for (OrderItem item : cart.getOrderItem()) {
        if (item.getProduct().getId().equals(productId)) {
          item.setKuantitas(item.getKuantitas() + quantity);
          item.setTotalHarga(item.getKuantitas() * product.getHarga());
          product.setStok(product.getStok() - quantity); // Update stok produk
          cartRepo.save(cart);
          msg.setMessage("Item updated in cart successfully");
          msg.setData(cart);
          return ResponseEntity.status(HttpStatus.OK).body(msg);
        }
      }

      OrderItem orderItem = new OrderItem();
      orderItem.setProduct(product);
      orderItem.setKuantitas(quantity);
      orderItem.setHargaSatuan(product.getHarga());
      orderItem.setTotalHarga(quantity * product.getHarga());
      orderItem.setCart(cart);
      product.setStok(product.getStok() - quantity); // Update stok produk

      cart.setOrderItem(orderItem);

      cartRepo.save(cart);

      msg.setMessage("Item added to cart successfully");
      msg.setData(cart);
      return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }
  }

  @PostMapping("/removeitem")
  public ResponseEntity<Object> removeItemFromCart(@RequestBody ItemInCart removeItemFromCart) {

    MessageModel msg = new MessageModel();

    try {
      String userId = removeItemFromCart.getUserId();
      String productId = removeItemFromCart.getProductId();
      int quantity = removeItemFromCart.getQuantity();

      Optional<Cart> optCart = cartRepo.findByUserId(userId);
      if (optCart.isEmpty()) {
        msg.setMessage("Cart not found for user: " + userId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Cart cart = optCart.get();

      Optional<Product> optProduct = productRepo.findById(productId);

      if (optProduct.isEmpty()) {
        msg.setMessage("Product tidak ditemukan");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Product product = optProduct.get();

      // Check if the product exists in the cart
      for (OrderItem item : cart.getOrderItem()) {
        if (item.getProduct().getId().equals(productId)) {
          if (quantity > item.getKuantitas()) {
            msg.setMessage("Cannot remove more than available quantity in cart");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
          } else if (quantity == item.getKuantitas()) {
            cart.getOrderItem().remove(item); // Remove item completely
            product.setStok(product.getStok() + quantity); // Update stok produk
            cartRepo.save(cart);
            msg.setMessage(product.getNama() + " removed from cart successfully");
            return ResponseEntity.status(HttpStatus.OK).body(msg);
          } else {
            // Reduce the quantity of the item
            item.setKuantitas(item.getKuantitas() - quantity);
            item.setTotalHarga(item.getKuantitas() * product.getHarga());
            product.setStok(product.getStok() + quantity); // Update stok produk
            cartRepo.save(cart);
            msg.setMessage("Item quantity updated in cart successfully");
            msg.setData(cart);
            return ResponseEntity.status(HttpStatus.OK).body(msg);
          }
        }
      }

      cartRepo.save(cart);

      return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }
  }

}
