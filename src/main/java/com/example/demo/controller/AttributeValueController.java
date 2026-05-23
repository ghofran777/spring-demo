package com.example.demo.controller;

import com.example.demo.dto.AttributeValueResponse;
import com.example.demo.dto.CreateAttributeValueRequest;
import com.example.demo.service.AttributeValueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attribute-values")
@CrossOrigin(origins = "*")
public class AttributeValueController {

    private final AttributeValueService attributeValueService;

    public AttributeValueController(AttributeValueService attributeValueService) {
        this.attributeValueService = attributeValueService;
    }

    @GetMapping
    public List<AttributeValueResponse> getAllAttributeValues() {
        return attributeValueService.getAllAttributeValues();
    }

    @GetMapping("/{id}")
    public AttributeValueResponse getAttributeValueById(@PathVariable UUID id) {
        return attributeValueService.getAttributeValueById(id);
    }

    @GetMapping("/attribute-definition/{attributeDefinitionId}")
    public List<AttributeValueResponse> getValuesByAttributeDefinition(
            @PathVariable UUID attributeDefinitionId
    ) {
        return attributeValueService.getValuesByAttributeDefinition(attributeDefinitionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AttributeValueResponse createAttributeValue(
            @Valid @RequestBody CreateAttributeValueRequest request
    ) {
        return attributeValueService.createAttributeValue(request);
    }

    @PutMapping("/{id}")
    public AttributeValueResponse updateAttributeValue(
            @PathVariable UUID id,
            @Valid @RequestBody CreateAttributeValueRequest request
    ) {
        return attributeValueService.updateAttributeValue(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttributeValue(@PathVariable UUID id) {
        attributeValueService.deleteAttributeValue(id);
    }
}
