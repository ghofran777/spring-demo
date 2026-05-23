package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AttributeValueResponse {

    private UUID id;
    private UUID attributeDefinitionId;
    private String attributeName;
    private String value;
    private Short sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AttributeValueResponse() {
    }

    public AttributeValueResponse(
            UUID id,
            UUID attributeDefinitionId,
            String attributeName,
            String value,
            Short sortOrder,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.attributeDefinitionId = attributeDefinitionId;
        this.attributeName = attributeName;
        this.value = value;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAttributeDefinitionId() {
        return attributeDefinitionId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getValue() {
        return value;
    }

    public Short getSortOrder() {
        return sortOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
