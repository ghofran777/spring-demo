package com.example.demo.repository;

import com.example.demo.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, UUID> {

    List<AttributeValue> findByAttributeDefinition_IdOrderBySortOrderAsc(UUID attributeDefinitionId);

    boolean existsByAttributeDefinition_IdAndValueIgnoreCase(UUID attributeDefinitionId, String value);
}
