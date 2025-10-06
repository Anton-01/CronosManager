package com.cronos.cronosmanager.model.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "credentials")
@Getter @Setter
@NoArgsConstructor
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String password;

    // Se actualiza cada vez que se cambia la contraseña
    private ZonedDateTime credentialUpdatedAt = ZonedDateTime.now();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Credential(String password, User user) {
        this.password = password;
        this.user = user;
    }
}
