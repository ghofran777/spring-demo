package com.example.demo.controller;

import com.example.demo.dto.CreateLocationStockRequest;
import com.example.demo.dto.LocationStockResponse;
import com.example.demo.service.LocationStockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/location-stocks")
@CrossOrigin(origins = "*")
public class LocationStockController {

    private final LocationStockService locationStockService;

    public LocationStockController(LocationStockService locationStockService) {
        this.locationStockService = locationStockService;
    }

    @GetMapping
    public List<LocationStockResponse> getAllLocationStocks() {
        return locationStockService.getAllLocationStocks();
    }

    @GetMapping("/{id}")
    public LocationStockResponse getLocationStockById(@PathVariable UUID id) {
        return locationStockService.getLocationStockById(id);
    }

    @GetMapping("/location/{locationId}")
    public List<LocationStockResponse> getLocationStocksByLocation(@PathVariable UUID locationId) {
        return locationStockService.getLocationStocksByLocation(locationId);
    }

    @GetMapping("/sku/{skuId}")
    public List<LocationStockResponse> getLocationStocksBySku(@PathVariable UUID skuId) {
        return locationStockService.getLocationStocksBySku(skuId);
    }

    @GetMapping("/location/{locationId}/sku/{skuId}")
    public LocationStockResponse getLocationStockByLocationAndSku(
            @PathVariable UUID locationId,
            @PathVariable UUID skuId
    ) {
        return locationStockService.getLocationStockByLocationAndSku(locationId, skuId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationStockResponse createLocationStock(
            @Valid @RequestBody CreateLocationStockRequest request
    ) {
        return locationStockService.createLocationStock(request);
    }

    @PutMapping("/{id}")
    public LocationStockResponse updateLocationStock(
            @PathVariable UUID id,
            @Valid @RequestBody CreateLocationStockRequest request
    ) {
        return locationStockService.updateLocationStock(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocationStock(@PathVariable UUID id) {
        locationStockService.deleteLocationStock(id);
    }
}
