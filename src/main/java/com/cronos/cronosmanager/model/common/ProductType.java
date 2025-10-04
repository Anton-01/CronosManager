package com.cronos.cronosmanager.model.common;

import com.cronos.cronosmanager.model.base.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Table(name = "product_types")
@Entity @Getter @Setter
@AttributeOverride(name = "id", column = @Column(name = "product_type_id"))
public class ProductType extends BaseEntity<UUID> {

    @Column(name = "type_name")
    private String name;

    @Column(name = "description")
    private String description;
}
