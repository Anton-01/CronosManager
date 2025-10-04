package com.cronos.cronosmanager.service.common;

import com.cronos.cronosmanager.dto.common.response.UnitOfMeasureResponseDto;
import com.cronos.cronosmanager.dto.common.request.UnitOfMeasureRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface IUnitMeasure {
    Page<UnitOfMeasureResponseDto> findAll(Pageable pageable);
    Optional<UnitOfMeasureResponseDto> findById(UUID id);
    UnitOfMeasureResponseDto save(UnitOfMeasureRequest unitOfMeasure);
    Optional<UnitOfMeasureResponseDto> update(UUID uuid, UnitOfMeasureRequest unitOfMeasure);
    void deleteById(UUID id);
}
