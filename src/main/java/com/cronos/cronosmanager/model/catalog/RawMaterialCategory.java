package com.cronos.cronosmanager.model.catalog;

import com.cronos.cronosmanager.model.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Table(name = "product_types")
@Entity @Getter @Setter
public class RawMaterialCategory extends BaseEntity<UUID> {
}
