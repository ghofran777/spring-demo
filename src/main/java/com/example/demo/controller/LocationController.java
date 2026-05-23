package com.example.demo.controller;

import com.example.demo.dto.CreateLocationRequest;
import com.example.demo.dto.LocationResponse;
import com.example.demo.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/locations")
    public List<LocationResponse> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/locations/{id}")
    public LocationResponse getLocationById(@PathVariable UUID id) {
        return locationService.getLocationById(id);
    }

    @GetMapping("/locations/merchant/{merchantId}")
    public List<LocationResponse> getLocationsByMerchant(@PathVariable UUID merchantId) {
        return locationService.getLocationsByMerchant(merchantId);
    }

    @PostMapping("/locations")
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponse createLocation(@Valid @RequestBody CreateLocationRequest request) {
        return locationService.createLocation(request);
    }

    @PutMapping("/locations/{id}")
    public LocationResponse updateLocation(@PathVariable UUID id,
                                           @Valid @RequestBody CreateLocationRequest request) {
        return locationService.updateLocation(id, request);
    }

    @DeleteMapping("/locations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocation(@PathVariable UUID id) {
        locationService.deleteLocation(id);
    }
}
