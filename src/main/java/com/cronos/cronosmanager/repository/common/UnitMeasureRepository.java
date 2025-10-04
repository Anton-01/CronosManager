package com.cronos.cronosmanager.repository.common;

import com.cronos.cronosmanager.model.common.UnitOfMeasure;
import com.cronos.cronosmanager.repository.base.SoftDeletableRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UnitMeasureRepository extends SoftDeletableRepository<UnitOfMeasure, UUID> {
    boolean existsByUnitName(String nameMeasure);
}
