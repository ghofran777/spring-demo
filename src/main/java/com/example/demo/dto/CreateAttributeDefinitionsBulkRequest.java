package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public class CreateAttributeDefinitionsBulkRequest {

    @NotNull(message = "Product id is required")
    private UUID productId;

    @NotEmpty(message = "Attribute names are required")
    private List<
            @NotBlank(message = "Attribute name is required")
            @Size(max = 25, message = "Attribute name cannot exceed 25 characters")
            String
            > names;

    public CreateAttributeDefinitionsBulkRequest() {
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
