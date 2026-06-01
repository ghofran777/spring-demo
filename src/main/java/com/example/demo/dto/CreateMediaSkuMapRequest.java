package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateMediaSkuMapRequest {

    @NotNull(message = "Media id is required")
    private UUID mediaId;

    @NotNull(message = "SKU id is required")
    private UUID skuId;

    public CreateMediaSkuMapRequest() {
    }

    public UUID getMediaId() {
        return mediaId;
    }

    public void setMediaId(UUID mediaId) {
        this.mediaId = mediaId;
    }

    public UUID getSkuId() {
        return skuId;
    }

    public void setSkuId(UUID skuId) {
        this.skuId = skuId;
    }
}
