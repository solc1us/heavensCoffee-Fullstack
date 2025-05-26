package heavenscoffee.mainapp.services;

import java.util.ArrayList;

public class OrderItem {

  public static ArrayList<OrderItem> OrderItems = new ArrayList<>();

  CartService cartService = new CartService();

  public OrderItem() {
    // cartService.testCart();
    // Constructor can be used to initialize any necessary data or perform setup.
  }

  public static void getOrderItem() {
    // This method is a placeholder and does not perform any action.
    // It can be implemented later if needed.
  }
}
