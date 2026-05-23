package com.example.demo.dto;

import java.util.UUID;

public class SkuValueResponse {

    private UUID attributeValueId;
    private String attributeName;
    private String value;
    private Short sortOrder;

    public SkuValueResponse() {
    }

    public SkuValueResponse(
            UUID attributeValueId,
            String attributeName,
            String value,
            Short sortOrder
    ) {
        this.attributeValueId = attributeValueId;
        this.attributeName = attributeName;
        this.value = value;
        this.sortOrder = sortOrder;
    }

    public UUID getAttributeValueId() {
        return attributeValueId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getValue() {
        return value;
    }

    public Short getSortOrder() {
        return sortOrder;
    }
}
