package com.example.demo.controller;

import com.example.demo.dto.CreateMediaSkuMapRequest;
import com.example.demo.dto.MediaSkuMapResponse;
import com.example.demo.service.MediaSkuMapService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/media-sku-map")
@CrossOrigin(origins = "*")
public class MediaSkuMapController {

    private final MediaSkuMapService mediaSkuMapService;

    public MediaSkuMapController(MediaSkuMapService mediaSkuMapService) {
        this.mediaSkuMapService = mediaSkuMapService;
    }

    @GetMapping
    public List<MediaSkuMapResponse> getAllMappings() {
        return mediaSkuMapService.getAllMappings();
    }

    @GetMapping("/{id}")
    public MediaSkuMapResponse getMappingById(@PathVariable UUID id) {
        return mediaSkuMapService.getMappingById(id);
    }

    @GetMapping("/media/{mediaId}")
    public List<MediaSkuMapResponse> getMappingsByMedia(@PathVariable UUID mediaId) {
        return mediaSkuMapService.getMappingsByMedia(mediaId);
    }

    @GetMapping("/sku/{skuId}")
    public List<MediaSkuMapResponse> getMappingsBySku(@PathVariable UUID skuId) {
        return mediaSkuMapService.getMappingsBySku(skuId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MediaSkuMapResponse createMapping(@Valid @RequestBody CreateMediaSkuMapRequest request) {
        return mediaSkuMapService.createMapping(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMapping(@PathVariable UUID id) {
        mediaSkuMapService.deleteMapping(id);
    }
}
