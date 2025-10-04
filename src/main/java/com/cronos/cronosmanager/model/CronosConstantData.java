package com.cronos.cronosmanager.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "cronos_constants_data")
public class CronosConstantData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "property_key", nullable = false)
    private String propertyKey;

    @Column(name = "property_value")
    private String propertyValue;

    @Column(nullable = false)
    private String profile;

    @Column(name = "process_identifier")
    private String processIdentifier;

    @Column(nullable = false)
    private boolean active;
}
