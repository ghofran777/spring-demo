package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class MediaSkuMapResponse {

    private UUID id;

    private UUID mediaId;
    private String mediaUrl;
    private String altText;

    private UUID skuId;
    private String skuRef;

    private UUID productId;
    private String productName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MediaSkuMapResponse(
            UUID id,
            UUID mediaId,
            String mediaUrl,
            String altText,
            UUID skuId,
            String skuRef,
            UUID productId,
            String productName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.mediaId = mediaId;
        this.mediaUrl = mediaUrl;
        this.altText = altText;
        this.skuId = skuId;
        this.skuRef = skuRef;
        this.productId = productId;
        this.productName = productName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getMediaId() {
        return mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getAltText() {
        return altText;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
