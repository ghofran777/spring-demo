package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SkuResponse {

    private UUID id;
    private UUID productId;
    private String productName;
    private String ref;
    private String barcode;
    private Boolean isActive;
    private List<SkuValueResponse> values;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SkuResponse() {
    }

    public SkuResponse(
            UUID id,
            UUID productId,
            String productName,
            String ref,
            String barcode,
            Boolean isActive,
            List<SkuValueResponse> values,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.ref = ref;
        this.barcode = barcode;
        this.isActive = isActive;
        this.values = values;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getRef() {
        return ref;
    }

    public String getBarcode() {
        return barcode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public List<SkuValueResponse> getValues() {
        return values;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
