package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateLocationRequest {

    @NotNull
    private UUID merchantId;

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @NotBlank
    private String address;

    private Boolean isActive;

    public CreateLocationRequest() {
    }

    public CreateLocationRequest(UUID merchantId, String name, String type, String address, Boolean isActive) {
        this.merchantId = merchantId;
        this.name = name;
        this.type = type;
        this.address = address;
        this.isActive = isActive;
    }

    public UUID getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(UUID merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
