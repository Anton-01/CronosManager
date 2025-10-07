package com.cronos.cronosmanager.model.common;

import com.cronos.cronosmanager.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter @Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 25, unique = true, nullable = false)
    private RoleEnum name;

    @Column(nullable = false)
    private String authority;

    public Role(RoleEnum name) {
        this.name = name;
        this.authority = name.name(); // Asignar la autoridad basada en el nombre del rol
    }
}
