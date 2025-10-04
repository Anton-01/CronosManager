package com.cronos.cronosmanager.dto.common.impl;

import com.cronos.cronosmanager.dto.common.response.UnitOfMeasureResponseDto;
import com.cronos.cronosmanager.dto.common.request.UnitOfMeasureRequest;
import com.cronos.cronosmanager.model.common.UnitOfMeasure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UnitOfMeasureMapper {

    UnitOfMeasureResponseDto toDto(UnitOfMeasure unitOfMeasure);

    @Mapping(target = "id", ignore = true)
    UnitOfMeasure toEntity(UnitOfMeasureRequest unitOfMeasureRequest);
}
