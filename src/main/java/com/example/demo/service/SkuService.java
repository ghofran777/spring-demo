package com.example.demo.service;

import com.example.demo.dto.CreateSkuRequest;
import com.example.demo.dto.SkuResponse;
import com.example.demo.dto.SkuValueResponse;
import com.example.demo.entity.AttributeDefinition;
import com.example.demo.entity.AttributeValue;
import com.example.demo.entity.Product;
import com.example.demo.entity.Sku;
import com.example.demo.entity.SkuAttribute;
import com.example.demo.repository.AttributeValueRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SkuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SkuService {

    private final SkuRepository skuRepository;
    private final ProductRepository productRepository;
    private final AttributeValueRepository attributeValueRepository;

    public SkuService(
            SkuRepository skuRepository,
            ProductRepository productRepository,
            AttributeValueRepository attributeValueRepository
    ) {
        this.skuRepository = skuRepository;
        this.productRepository = productRepository;
        this.attributeValueRepository = attributeValueRepository;
    }

    @Transactional(readOnly = true)
    public List<SkuResponse> getAllSkus() {
        return skuRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SkuResponse getSkuById(UUID id) {
        Sku sku = skuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "SKU not found"
                ));

        return toResponse(sku);
    }

    @Transactional(readOnly = true)
    public List<SkuResponse> getSkusByProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found"
            );
        }

        return skuRepository.findByProduct_Id(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SkuResponse createSku(CreateSkuRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));

        List<AttributeValue> attributeValues = new ArrayList<>();

        for (UUID attributeValueId : request.getAttributeValueIds()) {
            AttributeValue attributeValue = attributeValueRepository.findById(attributeValueId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Attribute value not found: " + attributeValueId
                    ));

            attributeValues.add(attributeValue);
        }

        validateAttributeValuesBelongToProduct(product, attributeValues);
        validateOneValuePerAttribute(attributeValues);
        validateSkuCombinationNotExists(product.getId(), attributeValues);

        List<AttributeValue> sortedValues = attributeValues.stream()
                .sorted(Comparator.comparing(value ->
                        normalizeAttributeName(value.getAttributeDefinition().getName())
                ))
                .toList();

        String ref = generateSkuRef(product, sortedValues);

        Sku sku = new Sku();

        sku.setProduct(product);
        sku.setRef(ref);
        sku.setBarcode(generateBarcode(ref));
        sku.setIsActive(request.getIsActive() == null ? true : request.getIsActive());

        for (AttributeValue attributeValue : sortedValues) {
            SkuAttribute skuAttribute = new SkuAttribute();

            skuAttribute.setSku(sku);
            skuAttribute.setAttributeValue(attributeValue);

            sku.getSkuAttributes().add(skuAttribute);
        }

        Sku savedSku = skuRepository.save(sku);

        return toResponse(savedSku);
    }

    @Transactional
    public void deleteSku(UUID id) {
        Sku sku = skuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "SKU not found"
                ));

        skuRepository.delete(sku);
    }

    private void validateAttributeValuesBelongToProduct(
            Product product,
            List<AttributeValue> attributeValues
    ) {
        for (AttributeValue attributeValue : attributeValues) {
            UUID valueProductId = attributeValue
                    .getAttributeDefinition()
                    .getProduct()
                    .getId();

            if (!valueProductId.equals(product.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Attribute value does not belong to this product"
                );
            }
        }
    }

    private void validateOneValuePerAttribute(List<AttributeValue> attributeValues) {
        Set<UUID> attributeDefinitionIds = new HashSet<>();

        for (AttributeValue attributeValue : attributeValues) {
            UUID attributeDefinitionId = attributeValue
                    .getAttributeDefinition()
                    .getId();

            if (!attributeDefinitionIds.add(attributeDefinitionId)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "You cannot choose two values for the same attribute"
                );
            }
        }
    }

    private void validateSkuCombinationNotExists(
            UUID productId,
            List<AttributeValue> requestedValues
    ) {
        Set<UUID> requestedValueIds = new HashSet<>();

        for (AttributeValue value : requestedValues) {
            requestedValueIds.add(value.getId());
        }

        List<Sku> existingSkus = skuRepository.findByProduct_Id(productId);

        for (Sku existingSku : existingSkus) {
            Set<UUID> existingValueIds = new HashSet<>();

            for (SkuAttribute skuAttribute : existingSku.getSkuAttributes()) {
                existingValueIds.add(skuAttribute.getAttributeValue().getId());
            }

            if (existingValueIds.equals(requestedValueIds)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "SKU already exists for this combination"
                );
            }
        }
    }

    private String generateSkuRef(Product product, List<AttributeValue> attributeValues) {
        String productPrefix = firstLetters(product.getName(), 3);

        StringBuilder ref = new StringBuilder(productPrefix);

        for (AttributeValue attributeValue : attributeValues) {
            AttributeDefinition attributeDefinition = attributeValue.getAttributeDefinition();

            String attributeName = normalizeAttributeName(attributeDefinition.getName());
            String valuePrefix = firstLetters(attributeValue.getValue(), 1);

            ref.append("-")
                    .append(attributeName)
                    .append("_")
                    .append(valuePrefix);
        }

        return ref.toString();
    }

    private String firstLetters(String text, int maxWords) {
        if (text == null || text.trim().isEmpty()) {
            return "X";
        }

        String[] words = text.trim().split("[^\\p{L}\\p{N}]+");

        StringBuilder result = new StringBuilder();
        int count = 0;

        for (String word : words) {
            if (word == null || word.isBlank()) {
                continue;
            }

            result.append(word.substring(0, 1).toUpperCase());
            count++;

            if (count >= maxWords) {
                break;
            }
        }

        if (result.length() == 0) {
            return "X";
        }

        return result.toString();
    }

    private String normalizeAttributeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "X";
        }

        String cleanName = name.trim().toLowerCase();

        if (cleanName.equals("couleur") || cleanName.equals("color")) {
            return "COLOR";
        }

        if (cleanName.equals("taille") || cleanName.equals("size")) {
            return "TAILLE";
        }

        return cleanName
                .toUpperCase()
                .replaceAll("[^\\p{L}\\p{N}]+", "_");
    }

    private String generateBarcode(String ref) {
        String randomCode = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        return ref + "-" + randomCode;
    }

    private SkuResponse toResponse(Sku sku) {
        Product product = sku.getProduct();

        List<SkuValueResponse> values = sku.getSkuAttributes()
                .stream()
                .sorted(Comparator.comparing(skuAttribute ->
                        normalizeAttributeName(
                                skuAttribute
                                        .getAttributeValue()
                                        .getAttributeDefinition()
                                        .getName()
                        )
                ))
                .map(skuAttribute -> {
                    AttributeValue attributeValue = skuAttribute.getAttributeValue();
                    AttributeDefinition attributeDefinition = attributeValue.getAttributeDefinition();

                    return new SkuValueResponse(
                            attributeValue.getId(),
                            attributeDefinition.getName(),
                            attributeValue.getValue(),
                            attributeValue.getSortOrder()
                    );
                })
                .toList();

        return new SkuResponse(
                sku.getId(),
                product.getId(),
                product.getName(),
                sku.getRef(),
                sku.getBarcode(),
                sku.getIsActive(),
                values,
                sku.getCreatedAt(),
                sku.getUpdatedAt()
        );
    }
}