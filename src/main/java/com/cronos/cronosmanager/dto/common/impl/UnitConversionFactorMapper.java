package com.cronos.cronosmanager.dto.common.impl;

import com.cronos.cronosmanager.dto.common.request.UnitConversionFactorRequestDto;
import com.cronos.cronosmanager.dto.common.response.UnitConversionFactorResponseDto;
import com.cronos.cronosmanager.model.common.UnitConversionFactor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UnitConversionFactorMapper {

    @Mapping(source = "fromUnit.unitName", target = "fromUnitName")
    @Mapping(source = "toUnit.unitName", target = "toUnitName")
    UnitConversionFactorResponseDto toDto(UnitConversionFactor unitConversionFactor);


    @Mapping(target = "fromUnit", ignore = true)
    @Mapping(target = "toUnit", ignore = true)
    @Mapping(target = "status", ignore = true)
    UnitConversionFactor toEntity(UnitConversionFactorRequestDto dto);
}
