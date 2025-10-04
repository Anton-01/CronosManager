package com.cronos.cronosmanager.dto.common.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UnitOfMeasureRequest {

    @NotBlank(message = "The name field cannot be empty.")
    @NotNull(message = "The name field cannot be null.")
    @Size(min = 5, max = 100, message = "The name field must be between 5 and 100 characters long.")
    private String unitName;

    @NotBlank(message = "The abbreviation field cannot be empty.")
    @NotNull(message = "The abbreviation field cannot be null.")
    @Size(min = 2, max = 10, message = "The abbreviation field must be between 2 and 10 characters long.")
    private String abbreviation;

    @NotBlank(message = "The unit type field cannot be empty.")
    @NotNull(message = "The unit type field cannot be null.")
    @Size(min = 5, max = 30, message = "The unit type field must be between 5 and 30 characters long.")
    private String unitType;
}
