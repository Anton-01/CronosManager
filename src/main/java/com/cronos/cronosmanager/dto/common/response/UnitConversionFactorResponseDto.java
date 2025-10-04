package com.cronos.cronosmanager.dto.common.response;

import com.cronos.cronosmanager.enums.EntityStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UnitConversionFactorResponseDto {

    private UUID id;
    private String fromUnitName;
    private String toUnitName;
    private BigDecimal factor;
    private EntityStatus status;
}
