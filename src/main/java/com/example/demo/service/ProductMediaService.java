package com.example.demo.service;

import com.example.demo.dto.CreateProductMediaRequest;
import com.example.demo.dto.ProductMediaResponse;
import com.example.demo.entity.MediaSkuMap;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductMedia;
import com.example.demo.repository.MediaSkuMapRepository;
import com.example.demo.repository.ProductMediaRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SkuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProductMediaService {

    private final ProductMediaRepository productMediaRepository;
    private final ProductRepository productRepository;
    private final SkuRepository skuRepository;
    private final MediaSkuMapRepository mediaSkuMapRepository;

    public ProductMediaService(
            ProductMediaRepository productMediaRepository,
            ProductRepository productRepository,
            SkuRepository skuRepository,
            MediaSkuMapRepository mediaSkuMapRepository
    ) {
        this.productMediaRepository = productMediaRepository;
        this.productRepository = productRepository;
        this.skuRepository = skuRepository;
        this.mediaSkuMapRepository = mediaSkuMapRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductMediaResponse> getAllMedia() {
        return productMediaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductMediaResponse getMediaById(UUID id) {
        ProductMedia media = productMediaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Media not found"
                ));

        return toResponse(media);
    }

    @Transactional(readOnly = true)
    public List<ProductMediaResponse> getMediaByProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found"
            );
        }

        return productMediaRepository.findByProduct_IdOrderBySortOrderAscCreatedAtAsc(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductMediaResponse> getActiveMediaByProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product not found"
            );
        }

        return productMediaRepository.findByProduct_IdAndIsActiveTrueOrderBySortOrderAscCreatedAtAsc(productId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductMediaResponse> getMediaBySku(UUID skuId) {
        if (!skuRepository.existsById(skuId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "SKU not found"
            );
        }

        return mediaSkuMapRepository.findBySku_Id(skuId)
                .stream()
                .map(MediaSkuMap::getMedia)
                .filter(media -> Boolean.TRUE.equals(media.getIsActive()))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProductMediaResponse createMedia(CreateProductMediaRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));

        ProductMedia media = new ProductMedia();

        media.setProduct(product);
        media.setUrl(cleanRequiredText(request.getUrl(), "URL is required"));
        media.setAltText(cleanOptionalText(request.getAltText()));
        media.setFilePath(cleanOptionalText(request.getFilePath()));
        media.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        media.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);

        ProductMedia savedMedia = productMediaRepository.save(media);

        return toResponse(savedMedia);
    }

    @Transactional
    public ProductMediaResponse updateMedia(UUID id, CreateProductMediaRequest request) {
        ProductMedia media = productMediaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Media not found"
                ));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));

        media.setProduct(product);
        media.setUrl(cleanRequiredText(request.getUrl(), "URL is required"));
        media.setAltText(cleanOptionalText(request.getAltText()));
        media.setFilePath(cleanOptionalText(request.getFilePath()));
        media.setIsActive(request.getIsActive() != null ? request.getIsActive() : media.getIsActive());
        media.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : media.getSortOrder());

        ProductMedia updatedMedia = productMediaRepository.save(media);

        return toResponse(updatedMedia);
    }

    @Transactional
    public void deleteMedia(UUID id) {
        ProductMedia media = productMediaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Media not found"
                ));

        mediaSkuMapRepository.deleteByMedia_Id(id);
        productMediaRepository.delete(media);
    }

    private ProductMediaResponse toResponse(ProductMedia media) {
        Product product = media.getProduct();

        return new ProductMediaResponse(
                media.getId(),
                product.getId(),
                product.getName(),
                media.getUrl(),
                media.getAltText(),
                media.getFilePath(),
                media.getIsActive(),
                media.getSortOrder(),
                media.getCreatedAt(),
                media.getUpdatedAt()
        );
    }

    private String cleanRequiredText(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    errorMessage
            );
        }

        return value.trim();
    }

    private String cleanOptionalText(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }
}
