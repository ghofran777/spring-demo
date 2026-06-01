package com.example.demo.repository;

import com.example.demo.entity.ProductMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductMediaRepository extends JpaRepository<ProductMedia, UUID> {

    List<ProductMedia> findByProduct_IdOrderBySortOrderAscCreatedAtAsc(UUID productId);

    List<ProductMedia> findByProduct_IdAndIsActiveTrueOrderBySortOrderAscCreatedAtAsc(UUID productId);
}
