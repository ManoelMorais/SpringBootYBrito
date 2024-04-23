package com.project.SpringBootYouTubeBrito.Controllers;

import com.project.SpringBootYouTubeBrito.Repositories.ProductRepositories;
import com.project.SpringBootYouTubeBrito.dtos.ProductRecordDto;
import com.project.SpringBootYouTubeBrito.models.ProductModels;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {

    @Autowired
    ProductRepositories productRepository;

    @PostMapping("/products")
    public ResponseEntity<ProductModels> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        var product = new ProductModels();
        BeanUtils.copyProperties(productRecordDto, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModels>> getAllProducts(){
        List<ProductModels> productsList = productRepository.findAll();

        if(!productsList.isEmpty()) {
            for(ProductModels product : productsList) {
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productsList);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value="id") UUID id) {
        Optional<ProductModels> productO = productRepository.findById(id);

        if (productO.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");

        productO.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("List of Products"));
        return ResponseEntity.status(HttpStatus.OK).body(productO.get());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable (value="id") UUID id, @RequestBody @Valid ProductRecordDto productDto){
        Optional<ProductModels> productO = productRepository.findById(id);

        if (productO.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");

        var product = productO.get();
        BeanUtils.copyProperties(productDto, product);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value="id") UUID id) {
        Optional<ProductModels> productO = productRepository.findById(id);
        if (productO.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        productRepository.delete(productO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully.");
    }
}
