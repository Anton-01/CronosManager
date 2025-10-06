package com.cronos.cronosmanager.dto.common.request;

import lombok.Data;

@Data
public class MfaRequestDto {
    private String username;
    private String code;
}
