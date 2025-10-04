package com.cronos.cronosmanager.dto.common.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@ToString(callSuper = true)
public class UnitConversionFactorRequestDto {

    @NotNull(message = "The 'from' unit ID cannot be null.")
    private UUID fromUnitId;

    @NotNull(message = "The 'to' unit ID cannot be null.")
    private UUID toUnitId;

    @NotNull(message = "The conversion factor cannot be null.")
    @Positive(message = "The conversion factor must be a positive number.")
    private BigDecimal factor;
}
