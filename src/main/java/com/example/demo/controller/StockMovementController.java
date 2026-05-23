package com.example.demo.controller;

import com.example.demo.dto.StockMovementResponse;
import com.example.demo.service.StockMovementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stock-movements")
@CrossOrigin(origins = "*")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @GetMapping
    public List<StockMovementResponse> getAllStockMovements() {
        return stockMovementService.getAllStockMovements();
    }

    @GetMapping("/{id}")
    public StockMovementResponse getStockMovementById(@PathVariable UUID id) {
        return stockMovementService.getStockMovementById(id);
    }

    @GetMapping("/location-stock/{locationStockId}")
    public List<StockMovementResponse> getMovementsByLocationStock(
            @PathVariable UUID locationStockId
    ) {
        return stockMovementService.getMovementsByLocationStock(locationStockId);
    }

    @GetMapping("/location/{locationId}")
    public List<StockMovementResponse> getMovementsByLocation(@PathVariable UUID locationId) {
        return stockMovementService.getMovementsByLocation(locationId);
    }

    @GetMapping("/sku/{skuId}")
    public List<StockMovementResponse> getMovementsBySku(@PathVariable UUID skuId) {
        return stockMovementService.getMovementsBySku(skuId);
    }
}
