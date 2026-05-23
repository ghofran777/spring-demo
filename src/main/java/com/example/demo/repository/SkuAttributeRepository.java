package com.example.demo.repository;

import com.example.demo.entity.SkuAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SkuAttributeRepository extends JpaRepository<SkuAttribute, UUID> {

    List<SkuAttribute> findBySku_Id(UUID skuId);
}
