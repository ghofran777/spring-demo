package com.example.demo.service;

import com.example.demo.dto.CreateLocationRequest;
import com.example.demo.dto.LocationResponse;
import com.example.demo.entity.Location;
import com.example.demo.entity.Merchant;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.MerchantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final MerchantRepository merchantRepository;

    public LocationService(LocationRepository locationRepository, MerchantRepository merchantRepository) {
        this.locationRepository = locationRepository;
        this.merchantRepository = merchantRepository;
    }

    public List<LocationResponse> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LocationResponse getLocationById(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));

        return toResponse(location);
    }

    public List<LocationResponse> getLocationsByMerchant(UUID merchantId) {
        return locationRepository.findByMerchant_Id(merchantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LocationResponse createLocation(CreateLocationRequest request) {
        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new RuntimeException("Merchant not found with id: " + request.getMerchantId()));

        Location location = new Location();
        location.setMerchant(merchant);
        location.setName(request.getName());
        location.setType(request.getType());
        location.setAddress(request.getAddress());
        location.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        Location savedLocation = locationRepository.save(location);

        return toResponse(savedLocation);
    }

    public LocationResponse updateLocation(UUID id, CreateLocationRequest request) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));

        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new RuntimeException("Merchant not found with id: " + request.getMerchantId()));

        location.setMerchant(merchant);
        location.setName(request.getName());
        location.setType(request.getType());
        location.setAddress(request.getAddress());
        location.setIsActive(request.getIsActive() != null ? request.getIsActive() : location.getIsActive());

        Location updatedLocation = locationRepository.save(location);

        return toResponse(updatedLocation);
    }

    public void deleteLocation(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));

        locationRepository.delete(location);
    }

    private LocationResponse toResponse(Location location) {
        Merchant merchant = location.getMerchant();

        return new LocationResponse(
                location.getId(),
                merchant.getId(),
                merchant.getName(),
                location.getName(),
                location.getType(),
                location.getAddress(),
                location.getIsActive(),
                location.getCreatedAt(),
                location.getUpdatedAt()
        );
    }
}
