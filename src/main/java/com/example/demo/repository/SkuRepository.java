package com.example.demo.repository;

import com.example.demo.entity.Sku;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SkuRepository extends JpaRepository<Sku, UUID> {

    List<Sku> findByProduct_Id(UUID productId);
}
