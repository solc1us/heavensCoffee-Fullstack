package heavenscoffee.mainapp.controllers;

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

import heavenscoffee.mainapp.models.Combo;
import heavenscoffee.mainapp.models.Kopi;
import heavenscoffee.mainapp.models.Product;
import heavenscoffee.mainapp.repos.ProductRepo;
import heavenscoffee.mainapp.utils.MessageModel;
import heavenscoffee.mainapp.utils.MessageModelPagination;
import heavenscoffee.mainapp.utils.SortingAndAscendingDescending;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  ProductRepo productRepo;

  @Autowired
  SortingAndAscendingDescending sortingAndAscendingDescending;

  @PostMapping("/createkopi")
  public ResponseEntity<Object> createKopi(@RequestBody Kopi data) {

    MessageModel msg = new MessageModel();

    try {

      if (data.getNama() == null || data.getNama().isEmpty()) {
        msg.setMessage("Nama is required in the request body.");
        return ResponseEntity.badRequest().body(msg);
      }

      if (data.getHarga() <= 0) {
        msg.setMessage("Harga is required in the request body and must be greater than 0.");
        return ResponseEntity.badRequest().body(msg);
      }

      if (data.getStok() < 0) {
        return ResponseEntity.badRequest().body("Stok is required in the request body and must be 0 or greater.");
      }

      if (data.getKategori() == null || data.getKategori().isEmpty()) {
        return ResponseEntity.badRequest().body("Kategori is required in the request body.");
      }

      if (productRepo.findByNama(data.getNama()).isPresent()) {
        msg.setMessage("Nama product already exists.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
      }

      Product product = new Kopi();

      product.setNama(data.getNama());
      product.setDeskripsi(data.getDeskripsi());
      product.setHarga(data.getHarga());
      product.setStok(data.getStok());
      ((Kopi) product).setKategori(data.getKategori());

      productRepo.save(product);

      msg.setMessage("Success");
      msg.setData(product);

      return ResponseEntity.status(HttpStatus.OK).body(msg);

    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
    }
  }

  @PostMapping("/createcombo")
  public ResponseEntity<Object> createCombo(@RequestBody Combo data) {

    MessageModel msg = new MessageModel();

    try {
      Product product = new Combo();

      product.setNama(data.getNama());
      product.setDeskripsi(data.getDeskripsi());
      product.setHarga(data.getHarga());
      product.setStok(data.getStok());

      productRepo.save(product);

      msg.setMessage("Success");
      msg.setData(product);

      return ResponseEntity.status(HttpStatus.OK).body(msg);

    } catch (

    Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
    }
  }

  @PutMapping("/updatekopi")
  public ResponseEntity<Object> updateKopi(@RequestBody Kopi param) {

    MessageModel msg = new MessageModel();

    try {

      if (param.getId().isEmpty() || param.getId().trim().length() < 1) {
        return ResponseEntity.badRequest().body("ID is required in the request body.");
      }

      Optional<Product> existingKopi = productRepo.findById(param.getId());

      if (existingKopi.isPresent()) {
        Product kopiToUpdate = existingKopi.get();

        // Update the fields of the kopi
        kopiToUpdate.setNama(param.getNama());
        kopiToUpdate.setDeskripsi(param.getDeskripsi());
        kopiToUpdate.setHarga(param.getHarga());
        kopiToUpdate.setStok(param.getStok());
        ((Kopi) kopiToUpdate).setKategori(((Kopi) param).getKategori());

        productRepo.save(kopiToUpdate);

        msg.setMessage("Sukses");
        msg.setData(kopiToUpdate);

      } else {
        msg.setMessage("Kopi dengan ID " + param.getId() + " tidak ditemukan.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      return ResponseEntity.status(HttpStatus.OK).body(msg);

    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }
  }

  @PutMapping("/updatecombo")
  public ResponseEntity<Object> updateCombo(@RequestBody Combo param) {

    MessageModel msg = new MessageModel();

    try {

      if (param.getId().isEmpty() || param.getId().trim().length() < 1) {
        return ResponseEntity.badRequest().body("ID is required in the request body.");
      }

      Optional<Product> existingCombo = productRepo.findById(param.getId());

      if (existingCombo.isPresent()) {
        Product comboToUpdate = existingCombo.get();

        // Update the fields of the combo
        comboToUpdate.setNama(param.getNama());
        comboToUpdate.setDeskripsi(param.getDeskripsi());
        comboToUpdate.setHarga(param.getHarga());
        comboToUpdate.setStok(param.getStok());

        productRepo.save(comboToUpdate);

        msg.setMessage("Sukses");
        msg.setData(comboToUpdate);

      } else {
        msg.setMessage("Combo dengan ID " + param.getId() + " tidak ditemukan.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      return ResponseEntity.status(HttpStatus.OK).body(msg);

    } catch (Exception e) {
      msg.setMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }
  }

  @GetMapping("/findall")
  public ResponseEntity<MessageModelPagination> findAllProductPagination(
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10") Integer size,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam(value = "urutan", required = false) String urutan,
      @RequestParam(value = "tipe", required = false) String tipe) {
    MessageModelPagination msg = new MessageModelPagination();
    try {
      Sort objSort = sortingAndAscendingDescending.getSortingData(sort, urutan);
      Pageable pageRequest = objSort == null ? PageRequest.of(page, size) : PageRequest.of(page, size, objSort);

      Page<Product> data = productRepo.findAll(pageRequest);

      if (tipe != null) {
        List<Product> filtered = new ArrayList<>();
        if (tipe.equalsIgnoreCase("kopi")) {
          for (Product product : data.getContent()) {
            if (product instanceof Kopi) {
              filtered.add(product);
            } else {
              filtered.remove(product);
            }
          }
        } else if (tipe.equalsIgnoreCase("combo")) {
          for (Product product : data.getContent()) {
            if (product instanceof Combo) {
              filtered.add(product);
            } else {
              filtered.remove(product);
            }
          }
        }

        msg.setMessage("Success");
        msg.setCurrentPage(data.getNumber());
        msg.setData(filtered);
        int filteredTotalItems = filtered.size();
        int filteredTotalPages = (int) Math.ceil((double) filteredTotalItems / size);
        msg.setTotalPages(filteredTotalPages);
        msg.setTotalItems(filteredTotalItems);
        msg.setNumberOfElement(filtered.size());
        return ResponseEntity.status(HttpStatus.OK).body(msg);
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

  @GetMapping("/findbyid/{id}")
  public ResponseEntity<Object> findProductById(
      @PathVariable("id") String id) {

    MessageModel msg = new MessageModel();

    try {

      if (id.isEmpty() || id == null) {
        msg.setMessage("'id' is required in the request param.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      Optional<Product> product = productRepo.findById(id);

      if (!product.isPresent() || product == null) {
        msg.setMessage("'id' yang anda masukkan salah (tidak ada di database).");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

      msg.setMessage("Sukses");
      msg.setData(product);
      return ResponseEntity.ok(msg);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @DeleteMapping("/deletebatchkopi")
  public ResponseEntity<Object> deleteKopi(@RequestBody List<Kopi> id) {
    productRepo.deleteAll(id);
    return ResponseEntity.status(HttpStatus.OK).body("Semua item berhasil dihapus");
  }

  @DeleteMapping("/deletebatchcombo")
  public ResponseEntity<Object> deleteCombo(@RequestBody List<Combo> id) {
    productRepo.deleteAll(id);
    return ResponseEntity.status(HttpStatus.OK).body("Semua item berhasil dihapus");
  }

  @DeleteMapping("/deletebyid/{id}")
  public ResponseEntity<Object> deleteById(@PathVariable("id") String id) {

    MessageModel msg = new MessageModel();

    try {

      Optional<Product> product = productRepo.findById(id);

      if (product.isPresent()) {

        productRepo.deleteById(id);

        msg.setMessage("Berhasil menghapus product yang namanya: " + product.get().getNama());
        msg.setData(product);
        return ResponseEntity.status(HttpStatus.OK).body(msg);
      } else {
        msg.setMessage("Tidak dapat menemukan product dengan id: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
      }

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

}
