package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AttributeDefinitionResponse {

    private UUID id;
    private UUID productId;
    private String productName;
    private String name;
    private List<AttributeValueResponse> values;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AttributeDefinitionResponse() {
    }

    public AttributeDefinitionResponse(
            UUID id,
            UUID productId,
            String productName,
            String name,
            List<AttributeValueResponse> values,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public List<AttributeValueResponse> getValues() {
        return values;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
