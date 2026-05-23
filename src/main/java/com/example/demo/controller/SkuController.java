package com.example.demo.controller;

import com.example.demo.dto.CreateSkuRequest;
import com.example.demo.dto.SkuResponse;
import com.example.demo.service.SkuService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skus")
@CrossOrigin(origins = "*")
public class SkuController {

    private final SkuService skuService;

    public SkuController(SkuService skuService) {
        this.skuService = skuService;
    }

    @GetMapping
    public List<SkuResponse> getAllSkus() {
        return skuService.getAllSkus();
    }

    @GetMapping("/{id}")
    public SkuResponse getSkuById(@PathVariable UUID id) {
        return skuService.getSkuById(id);
    }

    @GetMapping("/product/{productId}")
    public List<SkuResponse> getSkusByProduct(@PathVariable UUID productId) {
        return skuService.getSkusByProduct(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SkuResponse createSku(@Valid @RequestBody CreateSkuRequest request) {
        return skuService.createSku(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSku(@PathVariable UUID id) {
        skuService.deleteSku(id);
    }
}