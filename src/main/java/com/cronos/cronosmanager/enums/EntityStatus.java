package com.cronos.cronosmanager.enums;

public enum EntityStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    PENDING("PENDING"),
    SUSPENDED("SUSPENDED"),
    ARCHIVED("ARCHIVED"),
    DELETED("DELETED"),;

    private final String description;

    EntityStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
