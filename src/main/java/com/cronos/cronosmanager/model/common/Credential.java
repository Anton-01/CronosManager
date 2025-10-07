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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credential_id")
    private Long id;

    @Column(name = "credential_uuid", unique = true, nullable = false)
    private String credentialUuid = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String password;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Credential(String password, User user) {
        this.password = password;
        this.user = user;
    }
}
