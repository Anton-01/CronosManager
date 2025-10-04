package com.cronos.cronosmanager.model.base;

import com.cronos.cronosmanager.enums.EntityStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private T id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityStatus status = EntityStatus.ACTIVE;

    @CreatedDate
    @Column(name = "created_at",  updatable = false, nullable = false)
    private ZonedDateTime createdAt;

    @CreatedBy
    @Column(name = "created_user", updatable = false, nullable = false)
    private String createdUser;

    @LastModifiedDate
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_user")
    private String updatedUser;
}
