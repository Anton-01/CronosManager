package com.cronos.cronosmanager.service.common;

import com.cronos.cronosmanager.dto.common.request.ProductTypeRequestDto;
import com.cronos.cronosmanager.dto.common.response.ProductTypeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductTypeService {
    Page<ProductTypeResponseDto> findAll(Pageable pageable);
    Optional<ProductTypeResponseDto> findById(UUID id);
    ProductTypeResponseDto save(ProductTypeRequestDto productTypeRequestDto);
    Optional<ProductTypeResponseDto> update(UUID id, ProductTypeRequestDto requestDto);
    void deleteById(UUID id);
}
