package com.cronos.cronosmanager.dto.common.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Data
public class VatRateResponseDto {
    private UUID id;
    private String name;
    private BigDecimal rate;
    private Boolean isDefault;
    private Date effectiveDate;
    private Date endDate;
    private String status;
    private ZonedDateTime createdAt;
    private String createdUser;
}
