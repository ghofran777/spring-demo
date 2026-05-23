package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class LocationStockResponse {

    private UUID id;

    private UUID locationId;
    private String locationName;

    private UUID skuId;
    private String skuRef;
    private String skuBarcode;

    private UUID productId;
    private String productName;

    private Integer quantity;
    private Integer reserved;
    private Integer available;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LocationStockResponse(
            UUID id,
            UUID locationId,
            String locationName,
            UUID skuId,
            String skuRef,
            String skuBarcode,
            UUID productId,
            String productName,
            Integer quantity,
            Integer reserved,
            Integer available,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.locationId = locationId;
        this.locationName = locationName;
        this.skuId = skuId;
        this.skuRef = skuRef;
        this.skuBarcode = skuBarcode;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.reserved = reserved;
        this.available = available;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
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

    public String getSkuBarcode() {
        return skuBarcode;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getReserved() {
        return reserved;
    }

    public Integer getAvailable() {
        return available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
