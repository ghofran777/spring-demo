package com.example.demo.service;

import com.example.demo.dto.AttributeDefinitionResponse;
import com.example.demo.dto.AttributeValueResponse;
import com.example.demo.dto.CreateProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.entity.Merchant;
import com.example.demo.entity.Product;
import com.example.demo.repository.MerchantRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;

    public ProductService(
            ProductRepository productRepository,
            MerchantRepository merchantRepository
    ) {
        this.productRepository = productRepository;
        this.merchantRepository = merchantRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));

        return toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByMerchant(UUID merchantId) {
        if (!merchantRepository.existsById(merchantId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Merchant not found"
            );
        }

        return productRepository.findByMerchant_Id(merchantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Merchant not found"
                ));

        Product product = new Product();

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setIsActive(request.getIsActive() == null ? true : request.getIsActive());
        product.setMerchant(merchant);

        Product savedProduct = productRepository.save(product);

        return toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, CreateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));

        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Merchant not found"
                ));

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setIsActive(request.getIsActive() == null ? true : request.getIsActive());
        product.setMerchant(merchant);

        Product savedProduct = productRepository.save(product);

        return toResponse(savedProduct);
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));

        productRepository.delete(product);
    }

    private ProductResponse toResponse(Product product) {
        Merchant merchant = product.getMerchant();

        List<AttributeDefinitionResponse> attributes = product.getAttributeDefinitions()
                .stream()
                .map(attributeDefinition -> {
                    List<AttributeValueResponse> values = attributeDefinition.getValues()
                            .stream()
                            .map(attributeValue -> new AttributeValueResponse(
                                    attributeValue.getId(),
                                    attributeDefinition.getId(),
                                    attributeDefinition.getName(),
                                    attributeValue.getValue(),
                                    attributeValue.getSortOrder(),
                                    attributeValue.getCreatedAt(),
                                    attributeValue.getUpdatedAt()
                            ))
                            .toList();

                    return new AttributeDefinitionResponse(
                            attributeDefinition.getId(),
                            product.getId(),
                            product.getName(),
                            attributeDefinition.getName(),
                            values,
                            attributeDefinition.getCreatedAt(),
                            attributeDefinition.getUpdatedAt()
                    );
                })
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getIsActive(),
                merchant.getId(),
                merchant.getName(),
                attributes,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}