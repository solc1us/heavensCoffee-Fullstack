package heavenscoffee.mainapp.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import heavenscoffee.mainapp.dto.CreateOrderRequest;
import heavenscoffee.mainapp.models.Cart;
import heavenscoffee.mainapp.models.Invoice;
import heavenscoffee.mainapp.models.Order;
import heavenscoffee.mainapp.models.OrderItem;
import heavenscoffee.mainapp.models.Payment;
import heavenscoffee.mainapp.models.Shipping;
import heavenscoffee.mainapp.repos.*;
import heavenscoffee.mainapp.utils.MessageModel;
import heavenscoffee.mainapp.utils.MessageModelPagination;
import heavenscoffee.mainapp.utils.SortingAndAscendingDescending;

@RestController
@RequestMapping("/order")
public class OrderController {

  @Autowired
  OrderRepo orderRepo;

  @Autowired
  ProductRepo productRepo;

  @Autowired
  CartRepo cartRepo;

  @Autowired
  PaymentRepo paymentRepo;

  @Autowired
  ShippingRepo shippingRepo;

  @Autowired
  InvoiceRepo invoiceRepo;

  @Autowired
  SortingAndAscendingDescending sortingAndAscendingDescending;

  @PostMapping("/create")
  public ResponseEntity<Object> createOrder(@RequestBody CreateOrderRequest orderRequest) {

    MessageModel msg = new MessageModel();

    try {
      Order order = new Order();

      order.setUserId(orderRequest.getUserId());
      order.setAlamat(orderRequest.getAlamat());
      order.setMetodePembayaran(orderRequest.getMetodePembayaran());
      order.setStatusOrder("PENDING");

      // 1. Fetch the user's cart
      Optional<Cart> optCart = cartRepo.findByUserId(orderRequest.getUserId());
      if (optCart.isEmpty()) {
        msg.setMessage("Cart not found for user: " + orderRequest.getUserId());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }
      Cart cart = optCart.get();

      // 2. Get all items from the cart
      List<OrderItem> cartItems = cart.getOrderItem();
      if (cartItems == null || cartItems.isEmpty()) {
        msg.setMessage("Cart is empty.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
      }

      int totalTagihan = 0;
      for (OrderItem item : cartItems) {
        // 3. Assign the new order to each item
        item.setOrder(order);
        totalTagihan += item.getTotalHarga();
      }

      // 4. Assign items to the order
      order.setOrderItems(cartItems);
      order.setTotalTagihan(totalTagihan);
      order.setTanggalPemesanan(LocalDateTime.now());

      // 5. Save the order (cascades to items)
      orderRepo.save(order);

      msg.setMessage("Success");
      msg.setData(order);

      return ResponseEntity.status(HttpStatus.OK).body(msg);

    } catch (

    Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
    }
  }

  @PutMapping("/update")
  public ResponseEntity<Object> updateOrder(@RequestBody HashMap<String, String> payload) {

    MessageModel msg = new MessageModel();

    try {
      String orderId = payload.get("orderId");
      if (orderId == null || orderId.isEmpty()) {
        msg.setMessage("'orderId' is required in the request body.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
      }

      Optional<Order> orderOpt = orderRepo.findById(orderId);

      if (orderOpt.isEmpty()) {
        msg.setMessage("Order with ID " + orderId + " not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Order order = orderOpt.get();

      // Update the fields of the order
      if (payload.containsKey("statusPembayaran")) {
        order.setStatusOrder(payload.get("statusPembayaran"));
      }
      if (payload.containsKey("statusPembayaran") && payload.get("statusPembayaran").equals("PENDING")) {
        order.setTanggalPembayaran(null);
      }

      orderRepo.save(order);

      msg.setMessage("Order updated successfully.");
      msg.setData(order);
      return ResponseEntity.status(HttpStatus.OK).body(msg);

    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }
  }

  @PutMapping("/updateorder")
  public ResponseEntity<Object> updateOrder(@RequestBody Order param) {

    MessageModel msg = new MessageModel();

    try {

      if (param.getId().isEmpty() || param.getId().trim().length() < 1) {
        return ResponseEntity.badRequest().body("ID is required in the request body.");
      }

      Optional<Order> existingOrder = orderRepo.findById(param.getId());

      if (existingOrder.isPresent()) {
        Order orderToUpdate = existingOrder.get();

        // Update the fields of the order
        orderToUpdate.setStatusOrder(param.getStatusOrder());

        orderRepo.save(orderToUpdate);

        msg.setMessage("Sukses");
        msg.setData(orderToUpdate);

      } else {
        msg.setMessage("Order dengan ID " + param.getId() + " tidak ditemukan.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      return ResponseEntity.status(HttpStatus.OK).body(msg);

    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }
  }

  @PostMapping("/pay")
  public ResponseEntity<Object> payOrder(@RequestBody HashMap<String, String> payload) {

    MessageModel msg = new MessageModel();

    try {
      String orderId = payload.get("orderId");
      if (orderId == null || orderId.isEmpty()) {
        msg.setMessage("'orderId' is required in the request body.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
      }

      Optional<Order> orderOpt = orderRepo.findById(orderId);

      if (orderOpt.isEmpty()) {
        msg.setMessage("Order with ID " + orderId + " not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Order order = orderOpt.get();

      if ("PAID".equals(order.getStatusOrder())) {
        msg.setMessage("Order with ID " + orderId + " has already been paid.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
      }

      // Create payment
      Payment payment = new Payment();
      payment.setOrder(order);
      payment.setMetodePembayaran(order.getMetodePembayaran());
      payment.setTotalTagihan(order.getTotalTagihan());
      payment.setTanggalPembayaran(LocalDateTime.now());
      payment.setStatusPembayaran("PAID");
      // paymentRepo.save(payment);

      // Create invoice
      Invoice invoice = new Invoice();
      invoice.setOrder(order);
      invoice.setPayment(payment);
      invoice.setOrderItems(invoice.getOrder().getOrderItems());
      invoice.setTanggalDibuat(LocalDateTime.now());
      order.setInvoice(invoice);
      payment.setInvoice(invoice);

      // Update the order status to PAID
      order.setStatusOrder("PAID");
      order.setPayment(payment);
      order.setTanggalPembayaran(LocalDateTime.now());

      // Create shipping
      Shipping shipping = new Shipping();
      shipping.setAlamat(order.getAlamat());
      
      String randomResi = "R-ID000" + String.format("%09d", (int)(Math.random() * 1_000_000_000));
      shipping.setNomorResi(randomResi);
      shipping.setOrder(order);
      shipping.setStatusPengiriman("PENDING");
      order.setShipping(shipping);

      // shippingRepo.save(shipping);
      // invoiceRepo.save(invoice);
      orderRepo.save(order);

      order.getInvoice().setOrderItems(order.getOrderItems());

      msg.setMessage("Payment successful for order ID: " + orderId);
      msg.setData(order);
      return ResponseEntity.status(HttpStatus.OK).body(msg);

    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }
  }

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

      Page<Order> data = orderRepo.findAll(pageRequest);

      // Set orderItems in each invoice before returning
      for (Order order : data.getContent()) {
        if (order.getInvoice() != null) {
          order.getInvoice().setOrderItems(order.getOrderItems());
        }
      }

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

  @GetMapping("/find")
  public ResponseEntity<Object> findOrder() {

    MessageModel msg = new MessageModel();

    try {

      List<Order> orders = new ArrayList<>();

      msg.setMessage("Sukses");
      msg.setData(orders);
      return ResponseEntity.ok(msg);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping("/findbyid/{id}")
  public ResponseEntity<Object> findOrderById(
      @PathVariable("id") String id) {

    MessageModel msg = new MessageModel();

    try {

      if (id.isEmpty() || id == null) {
        msg.setMessage("'id' is required in the request param.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Optional<Order> order = orderRepo.findById(id);

      if (!order.isPresent() || order == null) {
        msg.setMessage("'id' yang anda masukkan salah (tidak ada di database).");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      msg.setMessage("Sukses");
      msg.setData(order);
      return ResponseEntity.ok(msg);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @DeleteMapping("/deletebatch")
  public ResponseEntity<Object> deleteBatch(@RequestBody List<Order> id) {
    orderRepo.deleteAll(id);
    return ResponseEntity.status(HttpStatus.OK).body("Semua item berhasil dihapus");
  }

  @DeleteMapping("/deletebyid/{id}")
  public ResponseEntity<Object> deleteById(@PathVariable("id") String id) {

    MessageModel msg = new MessageModel();

    try {

      Optional<Order> order = orderRepo.findById(id);

      if (order.isPresent()) {

        orderRepo.deleteById(id);

        msg.setMessage("Berhasil menghapus order dengan ID: " + order.get().getId() + " pada tanggal: "
            + order.get().getTanggalPemesanan());
        msg.setData(order);
        return ResponseEntity.status(HttpStatus.OK).body(msg);
      } else {
        msg.setMessage("Tidak dapat menemukan order dengan id: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

}
