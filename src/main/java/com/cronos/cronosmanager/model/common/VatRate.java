package com.cronos.cronosmanager.model.common;

import com.cronos.cronosmanager.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table(name = "vat_rates")
@Entity @Getter @Setter
@AttributeOverride(name = "id", column = @Column(name = "vat_rate_id"))
public class VatRate extends BaseEntity<UUID> {

    @Column(name = "name")
    private String name;

    @Column(name = "rate", precision = 5, scale = 2, nullable = false)
    private BigDecimal rate;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "effective_date")
    @Temporal(TemporalType.DATE)
    private LocalDate effectiveDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private LocalDate endDate;
}
