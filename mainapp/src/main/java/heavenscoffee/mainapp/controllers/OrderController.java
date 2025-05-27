package heavenscoffee.mainapp.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import heavenscoffee.mainapp.dto.RequestedItem;
import heavenscoffee.mainapp.models.Order;
import heavenscoffee.mainapp.models.OrderItem;
import heavenscoffee.mainapp.models.Product;
import heavenscoffee.mainapp.models.Order;
import heavenscoffee.mainapp.models.Order;
import heavenscoffee.mainapp.repos.OrderRepo;
import heavenscoffee.mainapp.repos.ProductRepo;
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
  SortingAndAscendingDescending sortingAndAscendingDescending;

  @PostMapping("/create")
  public ResponseEntity<Object> createOrder(@RequestBody CreateOrderRequest orderRequest) {

    MessageModel msg = new MessageModel();

    System.out.println("tes");

    try {
      Order order = new Order();

      order.setUserId(orderRequest.getUserId());
      order.setAlamat(orderRequest.getAlamat());
      order.setMetodePembayaran(orderRequest.getMetodePembayaran());
      order.setStatusOrder("PENDING");

      int totalTagihan = 0;

      List<OrderItem> orderItems = new ArrayList<>();
      for (RequestedItem itemDto : orderRequest.getItems()) {
        // 3. Ambil Product dari database (misal via productService)
        Optional<Product> optProduct = productRepo.findById(itemDto.getProductId());
        
        if (optProduct.isEmpty()) {
          msg.setMessage("Product tidak ditemukan");
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }

        Product product = optProduct.get();

        if (product.getStok() < itemDto.getQuantity()) {
          return ResponseEntity.badRequest().body("Stok tidak cukup untuk produk: " + product.getNama());
        }

        // 5. Buat OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product); // atau cuma productId dan info relevan lainnya
        orderItem.setKuantitas(itemDto.getQuantity());
        orderItem.setHargaSatuan(product.getHarga());
        orderItem.setTotalHarga(itemDto.getQuantity() * product.getHarga());
        totalTagihan += orderItem.getTotalHarga();
        product.setStok(product.getStok() - itemDto.getQuantity()); // Update stok produk
        orderItem.setOrder(order);

        orderItems.add(orderItem);
      }

      order.setOrderItems(orderItems);
      order.setTotalTagihan(totalTagihan);
      order.setTanggalPemesanan(LocalDateTime.now());

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

  @GetMapping("/findall")
  public ResponseEntity<MessageModelPagination> findAllOrderPagination(
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "urutan", required = false) String urutan,
      @RequestParam(value = "tipe", required = false) String tipe) {
    MessageModelPagination msg = new MessageModelPagination();
    try {
      Sort objSort = sortingAndAscendingDescending.getSortingData(sort, urutan);
      Pageable pageRequest = objSort == null ? PageRequest.of(page, size) : PageRequest.of(page, size, objSort);

      Page<Order> data = orderRepo.findAll(pageRequest);

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
