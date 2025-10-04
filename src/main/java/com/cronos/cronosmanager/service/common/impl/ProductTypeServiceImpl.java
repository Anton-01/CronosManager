package com.cronos.cronosmanager.service.common.impl;


import com.cronos.cronosmanager.dto.common.impl.ProductTypeMapper;
import com.cronos.cronosmanager.dto.common.request.ProductTypeRequestDto;
import com.cronos.cronosmanager.dto.common.response.ProductTypeResponseDto;
import com.cronos.cronosmanager.enums.EntityStatus;
import com.cronos.cronosmanager.exception.common.ResourceNotFoundException;
import com.cronos.cronosmanager.model.common.ProductType;
import com.cronos.cronosmanager.repository.common.ProductTypeRepository;
import com.cronos.cronosmanager.service.common.ProductTypeService;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private final Logger log = LoggerFactory.getLogger(ProductTypeServiceImpl.class);
    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeMapper productTypeMapper;
    private final Validator validator;

    @Transactional(readOnly = true)
    @Override
    public Page<ProductTypeResponseDto> findAll(Pageable pageable) {
        Page<ProductType> products = productTypeRepository.findAll(pageable);
        log.info("Find all products for page: {}", pageable);
        return products.map(productTypeMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ProductTypeResponseDto> findById(UUID id) {
        Objects.requireNonNull(id, "Id Of Product Type cannot be null");
        log.info("Find product type with id: {}", id);
        return productTypeRepository.findById(id).map(productTypeMapper::toDto);
    }

    @Transactional
    @Override
    public ProductTypeResponseDto save(ProductTypeRequestDto requestDto) {
        Objects.requireNonNull(requestDto, "The Request provided for Product Type cannot be null.");

        ProductType productType = productTypeMapper.toEntity(requestDto);
        validationsToSave(productType);
        log.info("Saving product type with id: {}", productType.getId());
        return productTypeMapper.toDto(productTypeRepository.save(productType));
    }

    @Transactional
    @Override
    public Optional<ProductTypeResponseDto> update(UUID id, ProductTypeRequestDto requestDto) {
        Objects.requireNonNull(id, "Id Of Product Type cannot be null");
        Objects.requireNonNull(requestDto, "The Request provided for Product Type cannot be null.");

        Optional<ProductType> existingProductOpt = productTypeRepository.findById(id);
        if (existingProductOpt.isEmpty()) {
            return Optional.empty();
        }

        ProductType existingProduct = existingProductOpt.get();
        validateEntity(existingProduct);
        mapperFieldsToUpdate(existingProduct, requestDto);
        log.info("Updating product type with id: {}", id);
        return Optional.of(productTypeMapper.toDto(productTypeRepository.save(existingProduct)));
    }

    @Transactional
    @Override
    public void deleteById(UUID id) {
        Objects.requireNonNull(id, "The ID to be deleted cannot be null.");

        ProductType product = productTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("The Product Type with ID %s does not exist.", id)));

        product.setStatus(EntityStatus.DELETED);
        productTypeRepository.save(product);
        log.info("Deleting product type with id: {}", id);
    }

    private void validationsToSave(ProductType productType){
        validateEntity(productType);
        if (productType.getId() != null) {
            throw new IllegalArgumentException("You cannot save a Product Type that already has an assigned ID. Use the update method instead.");
        }

        if (productTypeRepository.existsByNameIgnoreCase(productType.getName())) {
            throw new EntityExistsException(String.format("The Product Type with name '%s' already exists", productType.getName()));
        }
    }

    private void validateEntity(ProductType productType) {
        Set<ConstraintViolation<ProductType>> violations = validator.validate(productType);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void mapperFieldsToUpdate(ProductType productType, ProductTypeRequestDto requestDto) {
        productType.setName(requestDto.getName());
        productType.setDescription(requestDto.getDescription());
    }
}
