package com.example.demo.service;

import com.example.demo.dto.CreatePriceRequest;
import com.example.demo.dto.PriceResponse;
import com.example.demo.entity.Price;
import com.example.demo.entity.Product;
import com.example.demo.entity.Sku;
import com.example.demo.repository.PriceRepository;
import com.example.demo.repository.SkuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class PriceService {

    private final PriceRepository priceRepository;
    private final SkuRepository skuRepository;

    public PriceService(PriceRepository priceRepository, SkuRepository skuRepository) {
        this.priceRepository = priceRepository;
        this.skuRepository = skuRepository;
    }

    @Transactional(readOnly = true)
    public List<PriceResponse> getAllPrices() {
        return priceRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PriceResponse getPriceById(UUID id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Price not found"
                ));

        return toResponse(price);
    }

    @Transactional(readOnly = true)
    public List<PriceResponse> getPricesBySku(UUID skuId) {
        if (!skuRepository.existsById(skuId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "SKU not found"
            );
        }

        return priceRepository.findBySku_Id(skuId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PriceResponse> getActivePricesBySku(UUID skuId) {
        if (!skuRepository.existsById(skuId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "SKU not found"
            );
        }

        return priceRepository.findBySku_IdAndIsActiveTrue(skuId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PriceResponse getCurrentPriceBySku(UUID skuId) {
        if (!skuRepository.existsById(skuId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "SKU not found"
            );
        }

        Price price = priceRepository.findFirstBySku_IdAndIsActiveTrueOrderByCreatedAtDesc(skuId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Active price not found for this SKU"
                ));

        return toResponse(price);
    }

    @Transactional
    public PriceResponse createPrice(CreatePriceRequest request) {
        Sku sku = skuRepository.findById(request.getSkuId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "SKU not found"
                ));

        validateAmount(request.getAmount());

        Price price = new Price();
        price.setSku(sku);
        price.setLabel(cleanLabel(request.getLabel()));
        price.setAmount(request.getAmount());
        price.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        Price savedPrice = priceRepository.save(price);

        return toResponse(savedPrice);
    }

    @Transactional
    public PriceResponse updatePrice(UUID id, CreatePriceRequest request) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Price not found"
                ));

        Sku sku = skuRepository.findById(request.getSkuId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "SKU not found"
                ));

        validateAmount(request.getAmount());

        price.setSku(sku);
        price.setLabel(cleanLabel(request.getLabel()));
        price.setAmount(request.getAmount());
        price.setIsActive(request.getIsActive() != null ? request.getIsActive() : price.getIsActive());

        Price updatedPrice = priceRepository.save(price);

        return toResponse(updatedPrice);
    }

    @Transactional
    public void deletePrice(UUID id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Price not found"
                ));

        priceRepository.delete(price);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Amount must be greater than 0"
            );
        }
    }

    private String cleanLabel(String label) {
        if (label == null || label.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Label is required"
            );
        }

        return label.trim().toUpperCase();
    }

    private PriceResponse toResponse(Price price) {
        Sku sku = price.getSku();
        Product product = sku.getProduct();

        return new PriceResponse(
                price.getId(),
                sku.getId(),
                sku.getRef(),
                product.getId(),
                product.getName(),
                price.getLabel(),
                price.getAmount(),
                price.getIsActive(),
                price.getCreatedAt(),
                price.getUpdatedAt()
        );
    }
}
