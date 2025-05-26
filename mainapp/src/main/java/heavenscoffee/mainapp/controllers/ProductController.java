package heavenscoffee.mainapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import heavenscoffee.mainapp.repos.ProductRepo;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  ProductRepo productRepo;

  

}
