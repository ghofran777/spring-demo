package com.example.demo.service;

import com.example.demo.dto.CreateMerchantRequest;
import com.example.demo.dto.MerchantResponse;
import com.example.demo.entity.Merchant;
import com.example.demo.repository.MerchantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Transactional(readOnly = true)
    public List<MerchantResponse> getAllMerchants() {
        return merchantRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MerchantResponse getMerchantById(UUID id) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Merchant not found"
                ));

        return toResponse(merchant);
    }

    @Transactional
    public MerchantResponse createMerchant(CreateMerchantRequest request) {
        Merchant merchant = new Merchant();

        merchant.setName(request.getName());
        merchant.setCountry(request.getCountry());
        merchant.setIsActive(request.getIsActive() == null ? true : request.getIsActive());

        Merchant savedMerchant = merchantRepository.save(merchant);

        return toResponse(savedMerchant);
    }

    @Transactional
    public MerchantResponse updateMerchant(UUID id, CreateMerchantRequest request) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Merchant not found"
                ));

        merchant.setName(request.getName());
        merchant.setCountry(request.getCountry());
        merchant.setIsActive(request.getIsActive() == null ? true : request.getIsActive());

        Merchant savedMerchant = merchantRepository.save(merchant);

        return toResponse(savedMerchant);
    }

    @Transactional
    public void deleteMerchant(UUID id) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Merchant not found"
                ));

        merchantRepository.delete(merchant);
    }

    private MerchantResponse toResponse(Merchant merchant) {
        return new MerchantResponse(
                merchant.getId(),
                merchant.getName(),
                merchant.getIsActive(),
                merchant.getCountry(),
                merchant.getCreatedAt(),
                merchant.getUpdatedAt()
        );
    }
}