package com.example.demo.controller;

import com.example.demo.dto.CreateProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    
    
    @GetMapping("/products/{productId}")
    public ProductResponse getProductById(@PathVariable UUID productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/products/merchant/{merchantId}")
    public List<ProductResponse> getProductsByMerchant(@PathVariable UUID merchantId) {
        return productService.getProductsByMerchant(merchantId);
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        return productService.createProduct(request);
    }

    @PutMapping("/products/{productId}")
    public ProductResponse updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody CreateProductRequest request
    ) {
        return productService.updateProduct(productId, request);
    }

    @DeleteMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
    }
}