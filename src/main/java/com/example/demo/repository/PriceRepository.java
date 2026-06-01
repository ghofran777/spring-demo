package com.example.demo.repository;

import com.example.demo.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PriceRepository extends JpaRepository<Price, UUID> {

    List<Price> findBySku_Id(UUID skuId);

    List<Price> findBySku_IdAndIsActiveTrue(UUID skuId);

    Optional<Price> findFirstBySku_IdAndIsActiveTrueOrderByCreatedAtDesc(UUID skuId);
}
