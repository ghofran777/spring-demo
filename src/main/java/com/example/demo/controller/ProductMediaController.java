package com.example.demo.controller;

import com.example.demo.dto.CreateProductMediaRequest;
import com.example.demo.dto.ProductMediaResponse;
import com.example.demo.service.ProductMediaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-media")
@CrossOrigin(origins = "*")
public class ProductMediaController {

    private final ProductMediaService productMediaService;

    public ProductMediaController(ProductMediaService productMediaService) {
        this.productMediaService = productMediaService;
    }

    @GetMapping
    public List<ProductMediaResponse> getAllMedia() {
        return productMediaService.getAllMedia();
    }

    @GetMapping("/{id}")
    public ProductMediaResponse getMediaById(@PathVariable UUID id) {
        return productMediaService.getMediaById(id);
    }

    @GetMapping("/product/{productId}")
    public List<ProductMediaResponse> getMediaByProduct(@PathVariable UUID productId) {
        return productMediaService.getMediaByProduct(productId);
    }

    @GetMapping("/product/{productId}/active")
    public List<ProductMediaResponse> getActiveMediaByProduct(@PathVariable UUID productId) {
        return productMediaService.getActiveMediaByProduct(productId);
    }

    @GetMapping("/sku/{skuId}")
    public List<ProductMediaResponse> getMediaBySku(@PathVariable UUID skuId) {
        return productMediaService.getMediaBySku(skuId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductMediaResponse createMedia(@Valid @RequestBody CreateProductMediaRequest request) {
        return productMediaService.createMedia(request);
    }

    @PutMapping("/{id}")
    public ProductMediaResponse updateMedia(
            @PathVariable UUID id,
            @Valid @RequestBody CreateProductMediaRequest request
    ) {
        return productMediaService.updateMedia(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedia(@PathVariable UUID id) {
        productMediaService.deleteMedia(id);
    }
}
