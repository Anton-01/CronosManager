package com.cronos.cronosmanager.dto.common.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VatRateRequestDto {

    @Size(min = 5, max = 100, message = "The name field must be between 5 and 100 characters long.")
    @NotNull(message = "The name field cannot be null.")
    private String name;

    @NotNull(message = "The rate field cannot be null.")
    @Positive(message = "The rate field must be a positive number.")
    private BigDecimal rate;

    @NotNull(message = "The field default cannot be null.")
    private Boolean isDefault;

    @NotNull(message = "The effective date field cannot be null.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "The effective date must not be in the past.")
    private LocalDate effectiveDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "The end date must not be in the past.")
    private LocalDate endDate;
}
