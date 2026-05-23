package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CreateAttributeDefinitionRequest {

    @NotNull(message = "Product id is required")
    private UUID productId;

    @NotBlank(message = "Attribute name is required")
    @Size(max = 25, message = "Attribute name cannot exceed 25 characters")
    private String name;

    public CreateAttributeDefinitionRequest() {
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
