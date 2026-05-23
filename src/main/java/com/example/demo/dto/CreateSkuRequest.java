package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class CreateSkuRequest {

    @NotNull(message = "Product id is required")
    private UUID productId;

    @NotEmpty(message = "Attribute value ids are required")
    private List<UUID> attributeValueIds;

    private Boolean isActive;

    public CreateSkuRequest() {
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public List<UUID> getAttributeValueIds() {
        return attributeValueIds;
    }

    public void setAttributeValueIds(List<UUID> attributeValueIds) {
        this.attributeValueIds = attributeValueIds;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
