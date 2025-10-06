package com.cronos.cronosmanager.dto.common.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MfaVerificationRequestDto {
    @NotEmpty(message = "El correo electrónico no puede estar vacío.")
    @Email(message = "El formato del correo electrónico no es válido.")
    private String email;

    @NotEmpty(message = "El código MFA no puede estar vacío.")
    private String code;
}
