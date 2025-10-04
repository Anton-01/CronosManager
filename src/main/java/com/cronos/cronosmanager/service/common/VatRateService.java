package com.cronos.cronosmanager.service.common;

import com.cronos.cronosmanager.dto.common.request.VatRateRequestDto;
import com.cronos.cronosmanager.dto.common.response.VatRateResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface VatRateService {
    Page<VatRateResponseDto> findAll(Pageable pageable);
    Optional<VatRateResponseDto> findById(UUID id);
    VatRateResponseDto save(VatRateRequestDto vatRate);
    Optional<VatRateResponseDto> update(UUID id, VatRateRequestDto vatRate);
    void deleteById(UUID id);
}
