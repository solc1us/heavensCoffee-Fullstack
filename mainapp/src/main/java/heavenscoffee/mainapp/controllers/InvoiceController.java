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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import heavenscoffee.mainapp.models.Cart;
import heavenscoffee.mainapp.models.Invoice;
import heavenscoffee.mainapp.models.Order;
import heavenscoffee.mainapp.repos.InvoiceRepo;
import heavenscoffee.mainapp.repos.OrderRepo;
import heavenscoffee.mainapp.utils.MessageModel;
import heavenscoffee.mainapp.utils.MessageModelPagination;
import heavenscoffee.mainapp.utils.SortingAndAscendingDescending;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

  @Autowired
  SortingAndAscendingDescending sortingAndAscendingDescending;

  @Autowired
  InvoiceRepo invoiceRepo;

  @Autowired
  OrderRepo orderRepo;

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

      Page<Invoice> data = invoiceRepo.findAll(pageRequest);

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

  @GetMapping("/findbyorderid/{orderid}")
  public ResponseEntity<MessageModel> findByOrderId(
      @PathVariable("orderid") String orderId) {

    MessageModel msg = new MessageModel();

    try {

      if (orderId.isEmpty() || orderId == null) {
        msg.setMessage("'orderId' is required in the request param.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Optional<Order> optOrder = orderRepo.findById(orderId);
      
      if (!optOrder.isPresent() || optOrder == null) {
        msg.setMessage("'orderId' yang anda masukkan salah (tidak ada di database).");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Order order = optOrder.get();

      if (order.getInvoice() == null) {
        msg.setMessage("Order ini belum dibayar.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Invoice invoice = order.getInvoice();
      invoice.setOrderItems(order.getOrderItems());

      msg.setMessage("Sukses");
      msg.setData(invoice);
      return ResponseEntity.ok(msg);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

}
