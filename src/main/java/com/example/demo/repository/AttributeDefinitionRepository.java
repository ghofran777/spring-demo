package com.example.demo.repository;

import com.example.demo.entity.AttributeDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttributeDefinitionRepository extends JpaRepository<AttributeDefinition, UUID> {

    List<AttributeDefinition> findByProduct_Id(UUID productId);

    boolean existsByProduct_IdAndNameIgnoreCase(UUID productId, String name);

    Optional<AttributeDefinition> findByProduct_IdAndNameIgnoreCase(UUID productId, String name);

    List<AttributeDefinition> findByNameIgnoreCase(String name);
}