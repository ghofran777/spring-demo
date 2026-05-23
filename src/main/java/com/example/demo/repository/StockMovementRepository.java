package com.example.demo.repository;

import com.example.demo.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {

    List<StockMovement> findByLocationStock_IdOrderByCreatedAtDesc(UUID locationStockId);

    List<StockMovement> findByLocationStock_Location_IdOrderByCreatedAtDesc(UUID locationId);

    List<StockMovement> findByLocationStock_Sku_IdOrderByCreatedAtDesc(UUID skuId);
}
