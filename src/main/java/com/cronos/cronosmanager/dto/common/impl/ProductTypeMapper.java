package com.cronos.cronosmanager.dto.common.impl;

import com.cronos.cronosmanager.dto.common.request.ProductTypeRequestDto;
import com.cronos.cronosmanager.dto.common.response.ProductTypeResponseDto;
import com.cronos.cronosmanager.model.common.ProductType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {
    ProductTypeResponseDto toDto(ProductType productType);

    @Mapping(target = "id", ignore = true)
    ProductType toEntity(ProductTypeRequestDto productTypeRequestDto);
}
