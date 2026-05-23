package com.example.demo.repository;

import com.example.demo.entity.LocationStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationStockRepository extends JpaRepository<LocationStock, UUID> {

    List<LocationStock> findByLocation_Id(UUID locationId);

    List<LocationStock> findBySku_Id(UUID skuId);

    Optional<LocationStock> findByLocation_IdAndSku_Id(UUID locationId, UUID skuId);

    boolean existsByLocation_IdAndSku_Id(UUID locationId, UUID skuId);
}
