package com.cronos.cronosmanager.dto.common.impl;

import com.cronos.cronosmanager.dto.common.request.VatRateRequestDto;
import com.cronos.cronosmanager.dto.common.response.VatRateResponseDto;
import com.cronos.cronosmanager.model.common.VatRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VatRateMapper {
    VatRateResponseDto toDto(VatRate vatRate);

    @Mapping(target = "id", ignore = true)
    VatRate toEntity(VatRateRequestDto vatRateRequestDto);
}
