package com.cronos.cronosmanager.dto.common.response;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UnitOfMeasureResponseDto {

    private UUID id;
    private String unitName;
    private String abbreviation;
    private String unitType;
    private Date createdAt;
    private String createdUser;
}
