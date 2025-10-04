package com.cronos.cronosmanager.model.common;

import com.cronos.cronosmanager.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "unit_conversion_factors", uniqueConstraints = {@UniqueConstraint(columnNames = {"from_unit_id", "to_unit_id"})})
@NoArgsConstructor
@AllArgsConstructor
@Entity @Getter @Setter
@AttributeOverride(name = "id", column = @Column(name = "conversion_factor_id"))
public class UnitConversionFactor extends BaseEntity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_unit_id", nullable = false)
    private UnitOfMeasure fromUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_unit_id", nullable = false)
    private UnitOfMeasure toUnit;

    @Column(name = "factor", nullable = false, precision = 19, scale = 8)
    private BigDecimal factor;
}
