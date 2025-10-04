package com.cronos.cronosmanager.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EntityStatusConverter implements AttributeConverter<EntityStatus, String> {
    @Override
    public String convertToDatabaseColumn(EntityStatus status) {
        if (status == null) {
            return null;
        }
        return status.name();
    }

    @Override
    public EntityStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return EntityStatus.valueOf(dbData);
    }
}
