package com.soa.controllers;

import com.soa.model.ErrorResponse;
import com.soa.model.NewProduct;
import com.soa.model.Product;
import com.soa.services.ProductService;
import com.soa.utils.ResponseUtils;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ResponseUtils responseUtils;

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        try {
            if(page < 1 || size < 1) {
                return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Page number is out of bounds!");
            }
            List<Product> products = productService.getProducts(sort, filter, page, size);
            return ResponseEntity.ok(products);
        } catch (IllegalArgumentException e) {
            return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return responseUtils.buildResponseWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody @Valid NewProduct newProduct) {
        try {
            Product product = productService.postProduct(newProduct);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(400, "Price is under 0!", LocalDateTime.now().toString());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (ValidationException e) {
            ErrorResponse errorResponse = new ErrorResponse(405, "Validation Exception", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal server error", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/labwork")
    public ResponseEntity<?> getLabWork() {
        Product product = productService.getProductById(84);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(400, "Product with current id not found", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal server error", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Integer id, @RequestBody @Valid NewProduct updatedProduct) {
        try {
            Product product = productService.updateProductById(id, updatedProduct);
            return ResponseEntity.ok(product);
        } catch (ConstraintViolationException e) {
            ErrorResponse errorResponse = new ErrorResponse(400, "Incorrect input", LocalDateTime.now().toString());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (ValidationException e) {
            ErrorResponse errorResponse = new ErrorResponse(405, "Name can't be empty", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(404, "Product with current id not found", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal server error", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Integer id) {
        try {
            productService.getProductById(id);
            productService.deleteProductById(id);
            return ResponseEntity.noContent().build();
        } catch (ConstraintViolationException e) {
            ErrorResponse errorResponse = new ErrorResponse(400, "Invalid id value", LocalDateTime.now().toString());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(404, e.getMessage(), LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal server error", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/unitOfMeasure/{unitOfMeasure}")
    public ResponseEntity<?> deleteProductsByUnitOfMeasure(@PathVariable("unitOfMeasure") String unitOfMeasure) {
        try {
            productService.deleteAllProductsByUnitOfMeasure(unitOfMeasure);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = new ErrorResponse(400, "Invalid value of unitOfMeasure", LocalDateTime.now().toString());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal server error", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/min-price")
    public ResponseEntity<?> getProductWithMinPrice() {
        try {
            Product product = productService.getProductWithMinPrice();
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(500, e.getMessage(), LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/manufactureCost/{cost}")
    public ResponseEntity<?> getCountProductsByManufactureCost(@PathVariable("cost") Integer cost) {
        try {
            int count = productService.getCountOfProductsWithBiggerCost(cost);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            ErrorResponse errorResponse = new ErrorResponse(400, "ManufactureCost value must be greater than 0!", LocalDateTime.now().toString());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal server error", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}


