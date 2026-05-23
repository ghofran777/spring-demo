package com.example.demo.service;

import com.example.demo.dto.AttributeValueResponse;
import com.example.demo.dto.CreateAttributeValueRequest;
import com.example.demo.entity.AttributeDefinition;
import com.example.demo.entity.AttributeValue;
import com.example.demo.repository.AttributeDefinitionRepository;
import com.example.demo.repository.AttributeValueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AttributeValueService {

    private final AttributeValueRepository attributeValueRepository;
    private final AttributeDefinitionRepository attributeDefinitionRepository;

    public AttributeValueService(
            AttributeValueRepository attributeValueRepository,
            AttributeDefinitionRepository attributeDefinitionRepository
    ) {
        this.attributeValueRepository = attributeValueRepository;
        this.attributeDefinitionRepository = attributeDefinitionRepository;
    }

    @Transactional(readOnly = true)
    public List<AttributeValueResponse> getAllAttributeValues() {
        return attributeValueRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AttributeValueResponse getAttributeValueById(UUID id) {
        AttributeValue attributeValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attribute value not found"
                ));

        return toResponse(attributeValue);
    }

    @Transactional(readOnly = true)
    public List<AttributeValueResponse> getValuesByAttributeDefinition(UUID attributeDefinitionId) {
        if (!attributeDefinitionRepository.existsById(attributeDefinitionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Attribute definition not found"
            );
        }

        return attributeValueRepository
                .findByAttributeDefinition_IdOrderBySortOrderAsc(attributeDefinitionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AttributeValueResponse createAttributeValue(CreateAttributeValueRequest request) {
        AttributeDefinition attributeDefinition = attributeDefinitionRepository
                .findById(request.getAttributeDefinitionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attribute definition not found"
                ));

        String cleanValue = request.getValue().trim();

        if (attributeValueRepository.existsByAttributeDefinition_IdAndValueIgnoreCase(
                request.getAttributeDefinitionId(),
                cleanValue
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Attribute value already exists for this attribute definition"
            );
        }

        AttributeValue attributeValue = new AttributeValue();

        attributeValue.setAttributeDefinition(attributeDefinition);
        attributeValue.setValue(cleanValue);
        attributeValue.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());

        AttributeValue savedAttributeValue = attributeValueRepository.save(attributeValue);

        return toResponse(savedAttributeValue);
    }

    @Transactional
    public AttributeValueResponse updateAttributeValue(
            UUID id,
            CreateAttributeValueRequest request
    ) {
        AttributeValue attributeValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attribute value not found"
                ));

        AttributeDefinition attributeDefinition = attributeDefinitionRepository
                .findById(request.getAttributeDefinitionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attribute definition not found"
                ));

        String cleanValue = request.getValue().trim();

        boolean sameAttributeDefinition = attributeValue.getAttributeDefinition()
                .getId()
                .equals(request.getAttributeDefinitionId());

        boolean sameValue = attributeValue.getValue().equalsIgnoreCase(cleanValue);

        if ((!sameAttributeDefinition || !sameValue)
                && attributeValueRepository.existsByAttributeDefinition_IdAndValueIgnoreCase(
                request.getAttributeDefinitionId(),
                cleanValue
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Attribute value already exists for this attribute definition"
            );
        }

        attributeValue.setAttributeDefinition(attributeDefinition);
        attributeValue.setValue(cleanValue);
        attributeValue.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());

        AttributeValue savedAttributeValue = attributeValueRepository.save(attributeValue);

        return toResponse(savedAttributeValue);
    }

    @Transactional
    public void deleteAttributeValue(UUID id) {
        AttributeValue attributeValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attribute value not found"
                ));

        attributeValueRepository.delete(attributeValue);
    }

    private AttributeValueResponse toResponse(AttributeValue attributeValue) {
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
