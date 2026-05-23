package com.example.demo.controller;

import com.example.demo.dto.CreateMerchantRequest;
import com.example.demo.dto.MerchantResponse;
import com.example.demo.service.MerchantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/merchants")
@CrossOrigin(origins = "*")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping
    public List<MerchantResponse> getAllMerchants() {
        return merchantService.getAllMerchants();
    }

    @GetMapping("/{id}")
    public MerchantResponse getMerchantById(@PathVariable UUID id) {
        return merchantService.getMerchantById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MerchantResponse createMerchant(
            @Valid @RequestBody CreateMerchantRequest request
    ) {
        return merchantService.createMerchant(request);
    }

    @PutMapping("/{id}")
    public MerchantResponse updateMerchant(
            @PathVariable UUID id,
            @Valid @RequestBody CreateMerchantRequest request
    ) {
        return merchantService.updateMerchant(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMerchant(@PathVariable UUID id) {
        merchantService.deleteMerchant(id);
    }
}