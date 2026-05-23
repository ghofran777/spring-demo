package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProductResponse {

    private UUID id;
    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Boolean isActive;
    private UUID merchantId;
    private String merchantName;
    private List<AttributeDefinitionResponse> attributes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse() {
    }

    public ProductResponse(
            UUID id,
            String name,
            String category,
            String description,
            BigDecimal price,
            Integer stock,
            Boolean isActive,
            UUID merchantId,
            String merchantName,
            List<AttributeDefinitionResponse> attributes,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.isActive = isActive;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.attributes = attributes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public UUID getMerchantId() {
        return merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public List<AttributeDefinitionResponse> getAttributes() {
        return attributes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}