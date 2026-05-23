package com.example.demo.controller;

import com.example.demo.dto.AttributeDefinitionResponse;
import com.example.demo.dto.CreateAttributeDefinitionRequest;
import com.example.demo.service.AttributeDefinitionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attribute-definitions")
@CrossOrigin(origins = "*")
public class AttributeDefinitionController {

    private final AttributeDefinitionService attributeDefinitionService;

    public AttributeDefinitionController(AttributeDefinitionService attributeDefinitionService) {
        this.attributeDefinitionService = attributeDefinitionService;
    }

    @GetMapping
    public List<AttributeDefinitionResponse> getAllAttributeDefinitions() {
        return attributeDefinitionService.getAllAttributeDefinitions();
    }

    @GetMapping("/{id}")
    public AttributeDefinitionResponse getAttributeDefinitionById(
            @PathVariable UUID id
    ) {
        return attributeDefinitionService.getAttributeDefinitionById(id);
    }

    @GetMapping("/product/{productId}")
    public List<AttributeDefinitionResponse> getAttributesByProduct(
            @PathVariable UUID productId
    ) {
        return attributeDefinitionService.getAttributesByProduct(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AttributeDefinitionResponse createAttributeDefinition(
            @Valid @RequestBody CreateAttributeDefinitionRequest request
    ) {
        return attributeDefinitionService.createAttributeDefinition(request);
    }

    @PutMapping("/{id}")
    public AttributeDefinitionResponse updateAttributeDefinition(
            @PathVariable UUID id,
            @Valid @RequestBody CreateAttributeDefinitionRequest request
    ) {
        return attributeDefinitionService.updateAttributeDefinition(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttributeDefinition(@PathVariable UUID id) {
        attributeDefinitionService.deleteAttributeDefinition(id);
    }
}
