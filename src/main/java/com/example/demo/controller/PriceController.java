package com.example.demo.controller;

import com.example.demo.dto.CreatePriceRequest;
import com.example.demo.dto.PriceResponse;
import com.example.demo.service.PriceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prices")
@CrossOrigin(origins = "*")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public List<PriceResponse> getAllPrices() {
        return priceService.getAllPrices();
    }

    @GetMapping("/{id}")
    public PriceResponse getPriceById(@PathVariable UUID id) {
        return priceService.getPriceById(id);
    }

    @GetMapping("/sku/{skuId}")
    public List<PriceResponse> getPricesBySku(@PathVariable UUID skuId) {
        return priceService.getPricesBySku(skuId);
    }

    @GetMapping("/sku/{skuId}/active")
    public List<PriceResponse> getActivePricesBySku(@PathVariable UUID skuId) {
        return priceService.getActivePricesBySku(skuId);
    }

    @GetMapping("/sku/{skuId}/current")
    public PriceResponse getCurrentPriceBySku(@PathVariable UUID skuId) {
        return priceService.getCurrentPriceBySku(skuId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PriceResponse createPrice(@Valid @RequestBody CreatePriceRequest request) {
        return priceService.createPrice(request);
    }

    @PutMapping("/{id}")
    public PriceResponse updatePrice(
            @PathVariable UUID id,
            @Valid @RequestBody CreatePriceRequest request
    ) {
        return priceService.updatePrice(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrice(@PathVariable UUID id) {
        priceService.deletePrice(id);
    }
}
