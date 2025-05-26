package heavenscoffee.mainapp.services;

import java.util.ArrayList;

public class CartService {
  // public static ArrayList<Cart> Carts = new ArrayList<>();

  // private ArrayList<Product> products = ProductService.getAllProduct();

//   public static void testCart() {
//     System.out.println("Testing CartService...");
//   }

//   public static void initializeCart(User user) {
//     System.out.println("Cart initialized for user: " + user.getUsername());
//     Carts.add(new Cart(user.getId()));
//   }

//   public static void addProductToCart(int productNumber, int quantity, String userId) {
//     Cart cart = getCartByUserId(userId);
//     if (cart == null) {
//       System.out.println("Cart not found for user: " + userId);
//       return;
//     }
//     if (productNumber < 1 || productNumber > ProductService.getAllProduct().size()) {
//       System.out.println("Invalid product number.");
//       return;
//     }
//     Product product = ProductService.getAllProduct().get(productNumber - 1);
//     if (quantity <= 0) {
//       System.out.println("Invalid quantity. Please enter a positive number.");
//       return;
//     }
//     if (product.getStok() < quantity) {
//       System.out.println("Insufficient stock for product: " + product.getNama());
//       return;
//     }
//     // Update product stock
//     product.setStok(product.getStok() - quantity);

//     // Create an OrderItem and add it to the cart
//     OrderItem orderItem = new OrderItem(cart.getId(), product, quantity);

//     orderItem.showOrderItem();

//     cart.setOrderItem(orderItem);

//     // Logic to add a product to the cart
//     System.out.println("Menambahkan " + quantity + " " + product.getNama() + " ke dalam keranjang.");
//   }

//   public static Cart getCartByUserId(String userId) {
//     for (Cart cart : Carts) {
//       if (cart.getUserId().equals(userId)) {
//         return cart;
//       }
//     }
//     return null; // Placeholder return value
//   }
}
