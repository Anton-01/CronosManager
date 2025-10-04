package com.cronos.cronosmanager.model.common;

import com.cronos.cronosmanager.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Table(name = "units_of_measure")
@Entity @Getter @Setter
@AttributeOverride(name = "id", column = @Column(name = "unit_of_measure_id"))
public class UnitOfMeasure extends BaseEntity<UUID> {

    @Column(name = "unit_name", nullable = false, unique = true, length = 50)
    private String unitName;

    @Column(name = "abbreviation", nullable = false, unique = true, length = 10)
    private String abbreviation;

    @Column(name = "unit_type", nullable = false, length = 50)
    private String unitType; // E.g.: 'Weight', 'Volume', 'Count', 'Length'
}