package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateMerchantRequest {

    @NotBlank(message = "Merchant name is required")
    private String name;

    @NotBlank(message = "Country is required")
    private String country;

    private Boolean isActive;

    public CreateMerchantRequest() {
    }

    public CreateMerchantRequest(String name, String country, Boolean isActive) {
        this.name = name;
        this.country = country;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}