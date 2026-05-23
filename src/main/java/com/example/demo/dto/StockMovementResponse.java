package com.example.demo.dto;

import com.example.demo.entity.StockMovementType;

import java.time.LocalDateTime;
import java.util.UUID;

public class StockMovementResponse {

    private UUID id;

    private UUID locationStockId;

    private UUID locationId;
    private String locationName;

    private UUID skuId;
    private String skuRef;

    private UUID productId;
    private String productName;

    private StockMovementType movementType;
    private Integer quantity;

    private Integer quantityBefore;
    private Integer quantityAfter;

    private Integer reservedBefore;
    private Integer reservedAfter;

    private String reason;
    private String reference;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StockMovementResponse(
            UUID id,
            UUID locationStockId,
            UUID locationId,
            String locationName,
            UUID skuId,
            String skuRef,
            UUID productId,
            String productName,
            StockMovementType movementType,
            Integer quantity,
            Integer quantityBefore,
            Integer quantityAfter,
            Integer reservedBefore,
            Integer reservedAfter,
            String reason,
            String reference,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.locationStockId = locationStockId;
        this.locationId = locationId;
        this.locationName = locationName;
        this.skuId = skuId;
        this.skuRef = skuRef;
        this.productId = productId;
        this.productName = productName;
        this.movementType = movementType;
        this.quantity = quantity;
        this.quantityBefore = quantityBefore;
        this.quantityAfter = quantityAfter;
        this.reservedBefore = reservedBefore;
        this.reservedAfter = reservedAfter;
        this.reason = reason;
        this.reference = reference;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getLocationStockId() {
        return locationStockId;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public UUID getSkuId() {
        return skuId;
    }

    public String getSkuRef() {
        return skuRef;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public StockMovementType getMovementType() {
        return movementType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getQuantityBefore() {
        return quantityBefore;
    }

    public Integer getQuantityAfter() {
        return quantityAfter;
    }

    public Integer getReservedBefore() {
        return reservedBefore;
    }

    public Integer getReservedAfter() {
        return reservedAfter;
    }

    public String getReason() {
        return reason;
    }

    public String getReference() {
        return reference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
