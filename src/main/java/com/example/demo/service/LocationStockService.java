package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.CreateLocationStockRequest;
import com.example.demo.dto.LocationStockResponse;
import com.example.demo.entity.Location;
import com.example.demo.entity.LocationStock;
import com.example.demo.entity.Product;
import com.example.demo.entity.Sku;
import com.example.demo.entity.StockMovementType;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.LocationStockRepository;
import com.example.demo.repository.SkuRepository;

@Service
public class LocationStockService {

    private final LocationStockRepository locationStockRepository;
    private final LocationRepository locationRepository;
    private final SkuRepository skuRepository;
    private final StockMovementService stockMovementService;

    public LocationStockService(
            LocationStockRepository locationStockRepository,
            LocationRepository locationRepository,
            SkuRepository skuRepository,
            StockMovementService stockMovementService
    ) {
        this.locationStockRepository = locationStockRepository;
        this.locationRepository = locationRepository;
        this.skuRepository = skuRepository;
        this.stockMovementService = stockMovementService;
    }

    @Transactional(readOnly = true)
    public List<LocationStockResponse> getAllLocationStocks() {
        return locationStockRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocationStockResponse getLocationStockById(UUID id) {
        LocationStock locationStock = locationStockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Location stock not found"
                ));

        return toResponse(locationStock);
    }

    @Transactional(readOnly = true)
    public List<LocationStockResponse> getLocationStocksByLocation(UUID locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Location not found"
            );
        }

        return locationStockRepository.findByLocation_Id(locationId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LocationStockResponse> getLocationStocksBySku(UUID skuId) {
        if (!skuRepository.existsById(skuId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "SKU not found"
            );
        }

        return locationStockRepository.findBySku_Id(skuId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LocationStockResponse getLocationStockByLocationAndSku(UUID locationId, UUID skuId) {
        LocationStock locationStock = locationStockRepository.findByLocation_IdAndSku_Id(locationId, skuId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Location stock not found for this location and SKU"
                ));

        return toResponse(locationStock);
    }

    @Transactional
    public LocationStockResponse createLocationStock(CreateLocationStockRequest request) {
        if (locationStockRepository.existsByLocation_IdAndSku_Id(
                request.getLocationId(),
                request.getSkuId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Stock already exists for this location and SKU"
            );
        }

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Location not found"
                ));

        Sku sku = skuRepository.findById(request.getSkuId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "SKU not found"
                ));

        Integer quantity = request.getQuantity() == null ? 0 : request.getQuantity();
        Integer reserved = request.getReserved() == null ? 0 : request.getReserved();

        validateStock(quantity, reserved);

        LocationStock locationStock = new LocationStock();

        locationStock.setLocation(location);
        locationStock.setSku(sku);
        locationStock.setQuantity(quantity);
        locationStock.setReserved(reserved);

        LocationStock savedLocationStock = locationStockRepository.save(locationStock);

        stockMovementService.recordMovement(
                savedLocationStock,
                StockMovementType.INITIAL,
                quantity,
                0,
                quantity,
                0,
                reserved,
                cleanText(request.getReason(), "Initial stock"),
                cleanText(request.getReference(), null)
        );

        return toResponse(savedLocationStock);
    }

    @Transactional
    public LocationStockResponse updateLocationStock(UUID id, CreateLocationStockRequest request) {
        LocationStock locationStock = locationStockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Location stock not found"
                ));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Location not found"
                ));

        Sku sku = skuRepository.findById(request.getSkuId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "SKU not found"
                ));

        boolean locationChanged = !locationStock.getLocation().getId().equals(request.getLocationId());
        boolean skuChanged = !locationStock.getSku().getId().equals(request.getSkuId());

        if ((locationChanged || skuChanged)
                && locationStockRepository.existsByLocation_IdAndSku_Id(
                request.getLocationId(),
                request.getSkuId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Stock already exists for this location and SKU"
            );
        }

        Integer quantityBefore = locationStock.getQuantity();
        Integer reservedBefore = locationStock.getReserved();

        Integer quantityAfter = request.getQuantity() == null ? 0 : request.getQuantity();
        Integer reservedAfter = request.getReserved() == null ? 0 : request.getReserved();

        validateStock(quantityAfter, reservedAfter);

        locationStock.setLocation(location);
        locationStock.setSku(sku);
        locationStock.setQuantity(quantityAfter);
        locationStock.setReserved(reservedAfter);

        LocationStock savedLocationStock = locationStockRepository.save(locationStock);

        if (stockChanged(quantityBefore, quantityAfter, reservedBefore, reservedAfter)) {
            StockMovementType movementType = detectMovementType(
                    quantityBefore,
                    quantityAfter,
                    reservedBefore,
                    reservedAfter
            );

            Integer movementQuantity = detectMovementQuantity(
                    quantityBefore,
                    quantityAfter,
                    reservedBefore,
                    reservedAfter
            );

            stockMovementService.recordMovement(
                    savedLocationStock,
                    movementType,
                    movementQuantity,
                    quantityBefore,
                    quantityAfter,
                    reservedBefore,
                    reservedAfter,
                    cleanText(request.getReason(), "Stock updated"),
                    cleanText(request.getReference(), null)
            );
        }

        return toResponse(savedLocationStock);
    }

    @Transactional
    public void deleteLocationStock(UUID id) {
        LocationStock locationStock = locationStockRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Location stock not found"
                ));

        locationStockRepository.delete(locationStock);
    }

    private void validateStock(Integer quantity, Integer reserved) {
        if (quantity < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Quantity cannot be negative"
            );
        }

        if (reserved < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reserved cannot be negative"
            );
        }

        if (reserved > quantity) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Reserved cannot be greater than quantity"
            );
        }
    }

    private boolean stockChanged(
            Integer quantityBefore,
            Integer quantityAfter,
            Integer reservedBefore,
            Integer reservedAfter
    ) {
        return !quantityBefore.equals(quantityAfter)
                || !reservedBefore.equals(reservedAfter);
    }

    private StockMovementType detectMovementType(
            Integer quantityBefore,
            Integer quantityAfter,
            Integer reservedBefore,
            Integer reservedAfter
    ) {
        boolean quantityChanged = !quantityBefore.equals(quantityAfter);
        boolean reservedChanged = !reservedBefore.equals(reservedAfter);

        if (quantityChanged && !reservedChanged) {
            if (quantityAfter > quantityBefore) {
                return StockMovementType.IN;
            }

            return StockMovementType.OUT;
        }

        if (!quantityChanged && reservedChanged) {
            if (reservedAfter > reservedBefore) {
                return StockMovementType.RESERVED;
            }

            return StockMovementType.RELEASED;
        }

        return StockMovementType.ADJUSTMENT;
    }

    private Integer detectMovementQuantity(
            Integer quantityBefore,
            Integer quantityAfter,
            Integer reservedBefore,
            Integer reservedAfter
    ) {
        int quantityDiff = Math.abs(quantityAfter - quantityBefore);
        int reservedDiff = Math.abs(reservedAfter - reservedBefore);

        return Math.max(quantityDiff, reservedDiff);
    }

    private String cleanText(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }

        return value.trim();
    }

    private LocationStockResponse toResponse(LocationStock locationStock) {
        Location location = locationStock.getLocation();
        Sku sku = locationStock.getSku();
        Product product = sku.getProduct();

        Integer quantity = locationStock.getQuantity();
        Integer reserved = locationStock.getReserved();
        Integer available = quantity - reserved;

        return new LocationStockResponse(
                locationStock.getId(),
                location.getId(),
                location.getName(),
                sku.getId(),
                sku.getRef(),
                sku.getBarcode(),
                product.getId(),
                product.getName(),
                quantity,
                reserved,
                available,
                locationStock.getCreatedAt(),
                locationStock.getUpdatedAt()
        );
    }
}