package com.cronos.cronosmanager.repository.common;

import com.cronos.cronosmanager.model.common.UnitConversionFactor;
import com.cronos.cronosmanager.model.common.UnitOfMeasure;
import com.cronos.cronosmanager.repository.base.SoftDeletableRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnitConversionFactorRepository extends SoftDeletableRepository<UnitConversionFactor, UUID> {
    //check if a combination of ‘from’ and ‘to’ already exists
    boolean existsByFromUnitAndToUnit(UnitOfMeasure fromUnit, UnitOfMeasure toUnit);
    Optional<UnitConversionFactor> findByFromUnitAndToUnit(UnitOfMeasure fromUnit, UnitOfMeasure toUnit);

}
