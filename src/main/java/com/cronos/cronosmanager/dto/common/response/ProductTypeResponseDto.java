package com.cronos.cronosmanager.dto.common.response;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class ProductTypeResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String status;
    private ZonedDateTime createdAt;
    private String createdUser;
}
