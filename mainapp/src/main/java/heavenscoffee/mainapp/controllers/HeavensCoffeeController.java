
package heavenscoffee.mainapp.controllers;

import java.util.Scanner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import heavenscoffee.mainapp.models.Cart;
import heavenscoffee.mainapp.models.User;
import heavenscoffee.mainapp.services.CartService;
import heavenscoffee.mainapp.services.ProductService;
import heavenscoffee.mainapp.services.UserService;

@RestController
@RequestMapping("/main")
public class HeavensCoffeeController {

  // ==========================
  // Global variable
  // ==========================

  private static Scanner scanner = new Scanner(System.in);

  // ==========================
  // Initialize user
  // ==========================

  public static User initializeUser(String nama, String password, String email, String nomorHp, int role) {
    return new User(nama, password, email, nomorHp, nomorHp, role);
  }

  // ==========================
  // User Menu
  // ==========================

  private static void showUserMenu() {
    System.out.println("User Menu:");
    System.out.println("0. View Account");
    System.out.println("1. View Products");
    System.out.println("2. View Cart");
    System.out.println("3. Add Product to Cart");
    System.out.println("4. Remove Product from Cart");
    System.out.println("8. Logout");
    System.out.println("9. Exit");
  }

  // public static void runCustomerCase(User user) {
  //   System.out.println("Signed in as " + user.getUsername());
  //   int menu;
  //   do {
  //     showUserMenu();
  //     System.out.print("Select an option: ");
  //     menu = scanner.nextInt();
  //     if (menu == 0) {
  //       System.out.println("View Account");
  //       System.out.println("Account details: ");
  //       user.showUserInfo();
  //     } else if (menu == 1) {
  //       System.out.println("View Products");
  //       ProductService.showProductList();
  //     } else if (menu == 2) {
  //       System.out.println("View Cart");
  //       Cart cart = CartService.getCartByUserId(user.getId());
  //       cart.showCart();
  //     } else if (menu == 3) {
  //       System.out.println("Add Product to Cart");
  //       System.out.println("Enter product number to add to cart: ");
  //       int productNumber = scanner.nextInt();
  //       System.out.println("Enter quantity: ");
  //       int quantity = scanner.nextInt();
  //       CartService.addProductToCart(productNumber, quantity, user.getId());
  //     } else if (menu == 4) {
  //       System.out.println("Remove Product from Cart");
  //       // Implement remove product logic here
  //     } else if (menu == 8) {
  //       System.out.println("Logout");
  //       runOnboardingCase();
  //     } else if (menu == 9) {
  //       System.out.println("Exiting...");
  //     } else {
  //       System.out.println("Invalid option");
  //     }
  //   } while (menu != 9);
  // }

  // ==========================
  // Admin Menu
  // ==========================

  private static void showAdminMenu() {
    System.out.println("Admin Menu:");
    System.out.println("0. View Account");
    System.out.println("1. View Products");
    System.out.println("2. Add Product");
    System.out.println("3. Remove Product");
    System.out.println("4. View Orders");
    System.out.println("8. Logout");
    System.out.println("9. Exit");
  }

  // public static void runAdminCase(User user) {
  //   System.out.println("Signed in as " + user.getUsername());
  //   int menu;
  //   do {
  //     scanner = new Scanner(System.in);
  //     showAdminMenu();
  //     System.out.print("Select an option: ");
  //     menu = scanner.nextInt();
  //     if (menu == 0) {
  //       System.out.println("View Account");
  //       System.out.println("Account details: ");
  //       user.showUserInfo();
  //       break;
  //     } else if (menu == 1) {
  //       System.out.println("View Products");
  //       break;
  //     } else if (menu == 2) {
  //       System.out.println("Add Product");
  //       break;
  //     } else if (menu == 3) {
  //       System.out.println("Remove Product");
  //       break;
  //     } else if (menu == 4) {
  //       System.out.println("View Orders");
  //       break;
  //     } else if (menu == 8) {
  //       System.out.println("Logout");
  //       runOnboardingCase();
  //       break;
  //     } else if (menu == 9) {
  //       System.out.println("Exiting...");
  //       break;
  //     } else {
  //       System.out.println("Invalid option");
  //     }

  //   } while (menu != 9);

  // }

  // ==========================
  // Onboarding Menu
  // ==========================

  public static void showOnboardingMenu() {
    System.out.println("Welcome to Heaven's Coffee!");
    System.out.println("1. Login");
    System.out.println("2. Register");
    System.out.println("3. Exit");
  }

  // public static void runOnboardingCase() {
  //   int menu;

  //   do {
  //     showOnboardingMenu();
  //     System.out.print("Select an option: ");
  //     menu = scanner.nextInt();
  //     if (menu == 1) {
  //       // Simulate user login
  //       System.out.println("Simulating user login...");
  //       System.out.println("Enter username: ");

  //       // String inputUsername = scanner.next(); // Simulated user input
  //       String inputUsername = "solc1us"; // Simulated user input for testing
  //       // Simulate fetching user from service
  //       User user = UserService.getUserByUsername(inputUsername);
  //       System.out.print("Enter password: ");
  //       // String inputPassword = scanner.next(); // Simulated user input
  //       String inputPassword = "password123"; // Simulated user input for testing

  //       if (UserService.validateUser(inputUsername, inputPassword)) {
  //         switch (user.getRole()) {
  //           case 1:
  //             System.out.println("Role 1 selected");
  //             HeavensCoffeeController.runCustomerCase(user);
  //             break;
  //           case 2:
  //             System.out.println("Role 2 selected");
  //             HeavensCoffeeController.runAdminCase(user);
  //             break;
  //           default:
  //             System.out.println("Invalid role");
  //         }
  //       } else {
  //         System.out.println("Invalid username or password");
  //         System.out.println("Please try again.");
  //         // Optionally, you can redirect to the onboarding menu again
  //         runOnboardingCase();
  //         // Or you can exit the program
  //         // System.exit(0);
  //         // Or you can ask the user to register
  //         // System.out.println("Do you want to register? (y/n)");
  //       }
  //       break;
  //     } else if (menu == 2) {
  //       System.out.println("Register");
  //       // Implement registration logic here
  //       break;
  //     } else if (menu == 3) {
  //       System.out.println("Exiting...");
  //       System.exit(0);
  //       break;
  //     } else {
  //       System.out.println("Invalid option");
  //     }
  //   } while (menu != 3);
  // }

}
