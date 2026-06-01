package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PriceResponse {

    private UUID id;

    private UUID skuId;
    private String skuRef;

    private UUID productId;
    private String productName;

    private String label;
    private BigDecimal amount;
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PriceResponse(
            UUID id,
            UUID skuId,
            String skuRef,
            UUID productId,
            String productName,
            String label,
            BigDecimal amount,
            Boolean isActive,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.skuId = skuId;
        this.skuRef = skuRef;
        this.productId = productId;
        this.productName = productName;
        this.label = label;
        this.amount = amount;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
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

    public String getLabel() {
        return label;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
