package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CreateAttributeValueRequest {

    @NotNull(message = "Attribute definition id is required")
    private UUID attributeDefinitionId;

    @NotBlank(message = "Value is required")
    @Size(max = 255, message = "Value cannot exceed 255 characters")
    private String value;

    private Short sortOrder;

    public CreateAttributeValueRequest() {
    }

    public UUID getAttributeDefinitionId() {
        return attributeDefinitionId;
    }

    public void setAttributeDefinitionId(UUID attributeDefinitionId) {
        this.attributeDefinitionId = attributeDefinitionId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Short getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Short sortOrder) {
        this.sortOrder = sortOrder;
    }
}
