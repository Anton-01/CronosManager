package com.cronos.cronosmanager.dto.common.request;

import com.cronos.cronosmanager.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotEmpty(message = "El nombre no puede estar vacío.")
    private String firstName;

    @NotEmpty(message = "El apellido no puede estar vacío.")
    private String lastName;

    @NotEmpty(message = "El correo electrónico no puede estar vacío.")
    @Email(message = "El formato del correo electrónico no es válido.")
    private String email;

    @NotEmpty(message = "La contraseña no puede estar vacía.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    private String password;

    @NotNull(message = "Se debe especificar un rol.")
    private RoleEnum role;
}
