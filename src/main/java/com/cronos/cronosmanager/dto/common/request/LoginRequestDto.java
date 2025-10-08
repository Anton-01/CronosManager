package com.cronos.cronosmanager.dto.common.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginRequestDto {
    @NotEmpty(message = "The e-mail field cannot be empty")
    @Email(message = "The e-mail format is invalid.")
    private String email;

    @NotEmpty(message = "The password field cannot be empty.")
    private String password;
}
