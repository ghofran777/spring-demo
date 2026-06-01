package com.example.demo.service;

import com.example.demo.dto.CreateMediaSkuMapRequest;
import com.example.demo.dto.MediaSkuMapResponse;
import com.example.demo.entity.MediaSkuMap;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductMedia;
import com.example.demo.entity.Sku;
import com.example.demo.repository.MediaSkuMapRepository;
import com.example.demo.repository.ProductMediaRepository;
import com.example.demo.repository.SkuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class MediaSkuMapService {

    private final MediaSkuMapRepository mediaSkuMapRepository;
    private final ProductMediaRepository productMediaRepository;
    private final SkuRepository skuRepository;

    public MediaSkuMapService(
            MediaSkuMapRepository mediaSkuMapRepository,
            ProductMediaRepository productMediaRepository,
            SkuRepository skuRepository
    ) {
        this.mediaSkuMapRepository = mediaSkuMapRepository;
        this.productMediaRepository = productMediaRepository;
        this.skuRepository = skuRepository;
    }

    @Transactional(readOnly = true)
    public List<MediaSkuMapResponse> getAllMappings() {
        return mediaSkuMapRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MediaSkuMapResponse getMappingById(UUID id) {
        MediaSkuMap mapping = mediaSkuMapRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Media SKU mapping not found"
                ));

        return toResponse(mapping);
    }

    @Transactional(readOnly = true)
    public List<MediaSkuMapResponse> getMappingsByMedia(UUID mediaId) {
        if (!productMediaRepository.existsById(mediaId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Media not found"
            );
        }

        return mediaSkuMapRepository.findByMedia_Id(mediaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MediaSkuMapResponse> getMappingsBySku(UUID skuId) {
        if (!skuRepository.existsById(skuId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "SKU not found"
            );
        }

        return mediaSkuMapRepository.findBySku_Id(skuId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public MediaSkuMapResponse createMapping(CreateMediaSkuMapRequest request) {
        if (mediaSkuMapRepository.existsByMedia_IdAndSku_Id(
                request.getMediaId(),
                request.getSkuId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This media is already linked to this SKU"
            );
        }

        ProductMedia media = productMediaRepository.findById(request.getMediaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Media not found"
                ));

        Sku sku = skuRepository.findById(request.getSkuId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "SKU not found"
                ));

        if (!media.getProduct().getId().equals(sku.getProduct().getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Media product and SKU product must be the same"
            );
        }

        MediaSkuMap mapping = new MediaSkuMap();

        mapping.setMedia(media);
        mapping.setSku(sku);

        MediaSkuMap savedMapping = mediaSkuMapRepository.save(mapping);

        return toResponse(savedMapping);
    }

    @Transactional
    public void deleteMapping(UUID id) {
        MediaSkuMap mapping = mediaSkuMapRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Media SKU mapping not found"
                ));

        mediaSkuMapRepository.delete(mapping);
    }

    private MediaSkuMapResponse toResponse(MediaSkuMap mapping) {
        ProductMedia media = mapping.getMedia();
        Sku sku = mapping.getSku();
        Product product = sku.getProduct();

        return new MediaSkuMapResponse(
                mapping.getId(),
                media.getId(),
                media.getUrl(),
                media.getAltText(),
                sku.getId(),
                sku.getRef(),
                product.getId(),
                product.getName(),
                mapping.getCreatedAt(),
                mapping.getUpdatedAt()
        );
    }
}
