package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class MerchantResponse {

    private UUID id;
    private String name;
    private Boolean isActive;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MerchantResponse() {
    }

    public MerchantResponse(
            UUID id,
            String name,
            Boolean isActive,
            String country,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.country = country;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getCountry() {
        return country;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}