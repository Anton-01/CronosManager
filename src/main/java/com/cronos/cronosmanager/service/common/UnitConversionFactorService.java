package com.cronos.cronosmanager.service.common;

import com.cronos.cronosmanager.dto.common.request.UnitConversionFactorRequestDto;
import com.cronos.cronosmanager.dto.common.response.UnitConversionFactorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UnitConversionFactorService {
    Page<UnitConversionFactorResponseDto> findAll(Pageable pageable);
    Optional<UnitConversionFactorResponseDto> findById(UUID id);
    UnitConversionFactorResponseDto save(UnitConversionFactorRequestDto requestDTO);
    Optional<UnitConversionFactorResponseDto> update(UUID id, UnitConversionFactorRequestDto requestDTO);
    void deleteById(UUID id);
}
