package com.example.demo.service;

import com.example.demo.dto.AttributeDefinitionResponse;
import com.example.demo.dto.AttributeValueResponse;
import com.example.demo.dto.CreateAttributeDefinitionRequest;
import com.example.demo.dto.CreateAttributeDefinitionsBulkRequest;
import com.example.demo.entity.AttributeDefinition;
import com.example.demo.entity.AttributeValue;
import com.example.demo.entity.Product;
import com.example.demo.repository.AttributeDefinitionRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AttributeDefinitionService {

    private final AttributeDefinitionRepository attributeDefinitionRepository;
    private final ProductRepository productRepository;

    public AttributeDefinitionService(
            AttributeDefinitionRepository attributeDefinitionRepository,
            ProductRepository productRepository
    ) {
        this.attributeDefinitionRepository = attributeDefinitionRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<AttributeDefinitionResponse> getAllAttributeDefinitions() {
        return attributeDefinitionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AttributeDefinitionResponse getAttributeDefinitionById(UUID id) {
        AttributeDefinition attributeDefinition = attributeDefinitionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attribute definition not found"
                ));

        return toResponse(attributeDefinition);
    }

    @Transactional(readOnly = true)
    public List<AttributeDefinitionResponse> getAttributesByProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found"
            );
        }

        return attributeDefinitionRepository.findByProduct_Id(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AttributeDefinitionResponse getAttributeByProductAndName(
            UUID productId,
            String name
    ) {
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found"
            );
        }

        return attributeDefinitionRepository
                .findByProduct_IdAndNameIgnoreCase(productId, name.trim())
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attribute definition not found for this product"
                ));
    }

    @Transactional(readOnly = true)
    public List<AttributeDefinitionResponse> getAttributesByName(String name) {
        return attributeDefinitionRepository.findByNameIgnoreCase(name.trim())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AttributeDefinitionResponse createAttributeDefinition(
            CreateAttributeDefinitionRequest request
    ) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));

        String cleanName = request.getName().trim();

        if (attributeDefinitionRepository.existsByProduct_IdAndNameIgnoreCase(
                request.getProductId(),
                cleanName
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Attribute definition already exists for this product"
            );
        }

        AttributeDefinition attributeDefinition = new AttributeDefinition();

        attributeDefinition.setProduct(product);
        attributeDefinition.setName(cleanName);

        AttributeDefinition savedAttributeDefinition =
                attributeDefinitionRepository.save(attributeDefinition);

        return toResponse(savedAttributeDefinition);
    }

    @Transactional
    public List<AttributeDefinitionResponse> createAttributeDefinitionsBulk(
            CreateAttributeDefinitionsBulkRequest request
    ) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));

        Set<String> cleanNames = new LinkedHashSet<>();

        for (String name : request.getNames()) {
            String cleanName = name.trim();
            String normalizedName = cleanName.toLowerCase();

            if (!cleanNames.add(normalizedName)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Duplicate attribute name in request: " + cleanName
                );
            }

            if (attributeDefinitionRepository.existsByProduct_IdAndNameIgnoreCase(
                    request.getProductId(),
                    cleanName
            )) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Attribute definition already exists for this product: " + cleanName
                );
            }
        }

        List<AttributeDefinition> attributeDefinitions = request.getNames()
                .stream()
                .map(name -> {
                    AttributeDefinition attributeDefinition = new AttributeDefinition();

                    attributeDefinition.setProduct(product);
                    attributeDefinition.setName(name.trim());

                    return attributeDefinition;
                })
                .toList();

        return attributeDefinitionRepository.saveAll(attributeDefinitions)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AttributeDefinitionResponse updateAttributeDefinition(
            UUID id,
            CreateAttributeDefinitionRequest request
    ) {
        AttributeDefinition attributeDefinition = attributeDefinitionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attribute definition not found"
                ));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));

        String cleanName = request.getName().trim();

        boolean sameProduct = attributeDefinition.getProduct()
                .getId()
                .equals(request.getProductId());

        boolean sameName = attributeDefinition.getName()
                .equalsIgnoreCase(cleanName);

        if ((!sameProduct || !sameName)
                && attributeDefinitionRepository.existsByProduct_IdAndNameIgnoreCase(
                request.getProductId(),
                cleanName
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Attribute definition already exists for this product"
            );
        }

        attributeDefinition.setProduct(product);
        attributeDefinition.setName(cleanName);

        AttributeDefinition savedAttributeDefinition =
                attributeDefinitionRepository.save(attributeDefinition);

        return toResponse(savedAttributeDefinition);
    }

    @Transactional
    public void deleteAttributeDefinition(UUID id) {
        AttributeDefinition attributeDefinition = attributeDefinitionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attribute definition not found"
                ));

        attributeDefinitionRepository.delete(attributeDefinition);
    }

    private AttributeDefinitionResponse toResponse(AttributeDefinition attributeDefinition) {
        Product product = attributeDefinition.getProduct();

        List<AttributeValueResponse> values = attributeDefinition.getValues()
                .stream()
                .map(this::toValueResponse)
                .toList();

        return new AttributeDefinitionResponse(
                attributeDefinition.getId(),
                product.getId(),
                product.getName(),
                attributeDefinition.getName(),
                values,
                attributeDefinition.getCreatedAt(),
                attributeDefinition.getUpdatedAt()
        );
    }

    private AttributeValueResponse toValueResponse(AttributeValue attributeValue) {
        AttributeDefinition attributeDefinition = attributeValue.getAttributeDefinition();

        return new AttributeValueResponse(
                attributeValue.getId(),
                attributeDefinition.getId(),
                attributeDefinition.getName(),
                attributeValue.getValue(),
                attributeValue.getSortOrder(),
                attributeValue.getCreatedAt(),
                attributeValue.getUpdatedAt()
        );
    }
}