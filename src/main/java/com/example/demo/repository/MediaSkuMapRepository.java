package com.example.demo.repository;

import com.example.demo.entity.MediaSkuMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MediaSkuMapRepository extends JpaRepository<MediaSkuMap, UUID> {

    List<MediaSkuMap> findByMedia_Id(UUID mediaId);

    List<MediaSkuMap> findBySku_Id(UUID skuId);

    boolean existsByMedia_IdAndSku_Id(UUID mediaId, UUID skuId);

    void deleteByMedia_Id(UUID mediaId);
}
