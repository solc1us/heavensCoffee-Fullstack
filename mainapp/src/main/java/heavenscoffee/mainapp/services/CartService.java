package heavenscoffee.mainapp.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import heavenscoffee.mainapp.models.Cart;
import heavenscoffee.mainapp.models.Order;
import heavenscoffee.mainapp.models.User;
import heavenscoffee.mainapp.repos.CartRepo;
import heavenscoffee.mainapp.utils.MessageModel;
import jakarta.transaction.TransactionScoped;

@Service
@TransactionScoped
public class CartService {

  @Autowired
  CartRepo cartRepo;

  // public static ArrayList<Cart> Carts = new ArrayList<>();

  // private ArrayList<Product> products = ProductService.getAllProduct();

  // public static void addProductToCart(int productNumber, int quantity, String
  // userId) {
  // Cart cart = getCartByUserId(userId);
  // if (cart == null) {
  // System.out.println("Cart not found for user: " + userId);
  // return;
  // }
  // if (productNumber < 1 || productNumber >
  // ProductService.getAllProduct().size()) {
  // System.out.println("Invalid product number.");
  // return;
  // }
  // Product product = ProductService.getAllProduct().get(productNumber - 1);
  // if (quantity <= 0) {
  // System.out.println("Invalid quantity. Please enter a positive number.");
  // return;
  // }
  // if (product.getStok() < quantity) {
  // System.out.println("Insufficient stock for product: " + product.getNama());
  // return;
  // }
  // // Update product stock
  // product.setStok(product.getStok() - quantity);

  // // Create an OrderItem and add it to the cart
  // OrderItem orderItem = new OrderItem(cart.getId(), product, quantity);

  // orderItem.showOrderItem();

  // cart.setOrderItem(orderItem);

  // // Logic to add a product to the cart
  // System.out.println("Menambahkan " + quantity + " " + product.getNama() + " ke
  // dalam keranjang.");
  // }

  // public static Cart getCartByUserId(String userId) {
  // for (Cart cart : Carts) {
  // if (cart.getUserId().equals(userId)) {
  // return cart;
  // }
  // }
  // return null; // Placeholder return value
  // }
}
