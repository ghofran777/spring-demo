package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductMediaResponse {

    private UUID id;

    private UUID productId;
    private String productName;

    private String url;
    private String altText;
    private String filePath;
    private Boolean isActive;
    private Integer sortOrder;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductMediaResponse(
            UUID id,
            UUID productId,
            String productName,
            String url,
            String altText,
            String filePath,
            Boolean isActive,
            Integer sortOrder,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.url = url;
        this.altText = altText;
        this.filePath = filePath;
        this.isActive = isActive;
        this.sortOrder = sortOrder;
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

    public String getUrl() {
        return url;
    }

    public String getAltText() {
        return altText;
    }

    public String getFilePath() {
        return filePath;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
