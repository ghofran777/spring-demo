package com.example.demo.service;

import com.example.demo.dto.StockMovementResponse;
import com.example.demo.entity.Location;
import com.example.demo.entity.LocationStock;
import com.example.demo.entity.Product;
import com.example.demo.entity.Sku;
import com.example.demo.entity.StockMovement;
import com.example.demo.entity.StockMovementType;
import com.example.demo.repository.LocationStockRepository;
import com.example.demo.repository.StockMovementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final LocationStockRepository locationStockRepository;

    public StockMovementService(
            StockMovementRepository stockMovementRepository,
            LocationStockRepository locationStockRepository
    ) {
        this.stockMovementRepository = stockMovementRepository;
        this.locationStockRepository = locationStockRepository;
    }

    @Transactional(readOnly = true)
    public List<StockMovementResponse> getAllStockMovements() {
        return stockMovementRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StockMovementResponse getStockMovementById(UUID id) {
        StockMovement stockMovement = stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Stock movement not found"
                ));

        return toResponse(stockMovement);
    }

    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsByLocationStock(UUID locationStockId) {
        if (!locationStockRepository.existsById(locationStockId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Location stock not found"
            );
        }

        return stockMovementRepository.findByLocationStock_IdOrderByCreatedAtDesc(locationStockId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsByLocation(UUID locationId) {
        return stockMovementRepository.findByLocationStock_Location_IdOrderByCreatedAtDesc(locationId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StockMovementResponse> getMovementsBySku(UUID skuId) {
        return stockMovementRepository.findByLocationStock_Sku_IdOrderByCreatedAtDesc(skuId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void recordMovement(
            LocationStock locationStock,
            StockMovementType movementType,
            Integer movementQuantity,
            Integer quantityBefore,
            Integer quantityAfter,
            Integer reservedBefore,
            Integer reservedAfter,
            String reason,
            String reference
    ) {
        StockMovement stockMovement = new StockMovement();

        stockMovement.setLocationStock(locationStock);
        stockMovement.setMovementType(movementType);
        stockMovement.setQuantity(movementQuantity);
        stockMovement.setQuantityBefore(quantityBefore);
        stockMovement.setQuantityAfter(quantityAfter);
        stockMovement.setReservedBefore(reservedBefore);
        stockMovement.setReservedAfter(reservedAfter);
        stockMovement.setReason(reason);
        stockMovement.setReference(reference);

        stockMovementRepository.save(stockMovement);
    }

    private StockMovementResponse toResponse(StockMovement stockMovement) {
        LocationStock locationStock = stockMovement.getLocationStock();
        Location location = locationStock.getLocation();
        Sku sku = locationStock.getSku();
        Product product = sku.getProduct();

        return new StockMovementResponse(
                stockMovement.getId(),
                locationStock.getId(),
                location.getId(),
                location.getName(),
                sku.getId(),
                sku.getRef(),
                product.getId(),
                product.getName(),
                stockMovement.getMovementType(),
                stockMovement.getQuantity(),
                stockMovement.getQuantityBefore(),
                stockMovement.getQuantityAfter(),
                stockMovement.getReservedBefore(),
                stockMovement.getReservedAfter(),
                stockMovement.getReason(),
                stockMovement.getReference(),
                stockMovement.getCreatedAt(),
                stockMovement.getUpdatedAt()
        );
    }
}
