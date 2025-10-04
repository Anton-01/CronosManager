package com.cronos.cronosmanager.dto.common.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductTypeRequestDto {

    @Size(min = 5, max = 100, message = "The name field must be between 5 and 100 characters long.")
    @NotNull(message = "The name field cannot be null.")
    private String name;

    @NotNull(message = "The string field cannot be null.")
    private String description;

    public void setName(String name) {
        this.name = (name != null) ? name.trim() : null;
    }

    public void setDescription(String description) {
        this.description = (description != null) ? description.trim() : null;
    }
}
