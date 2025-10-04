package com.cronos.cronosmanager.service.common.impl;

import com.cronos.cronosmanager.dto.common.response.UnitOfMeasureResponseDto;
import com.cronos.cronosmanager.dto.common.impl.UnitOfMeasureMapper;
import com.cronos.cronosmanager.dto.common.request.UnitOfMeasureRequest;
import com.cronos.cronosmanager.enums.EntityStatus;
import com.cronos.cronosmanager.exception.common.ResourceNotFoundException;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import com.cronos.cronosmanager.model.common.UnitOfMeasure;
import com.cronos.cronosmanager.repository.common.UnitMeasureRepository;
import com.cronos.cronosmanager.service.common.IUnitMeasure;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UnitMeasureServiceImpl implements IUnitMeasure {

    private final UnitMeasureRepository unitMeasureRepository;
    private final UnitOfMeasureMapper unitOfMeasureMapper;
    private final Validator validator;

    @Transactional(readOnly = true)
    @Override
    public Page<UnitOfMeasureResponseDto> findAll(Pageable pageable) {
        Page<UnitOfMeasure> pageUnits = unitMeasureRepository.findAll(pageable);
        return pageUnits.map(unitOfMeasureMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UnitOfMeasureResponseDto> findById(UUID id) {
        Objects.requireNonNull(id, "Id of UnitOfMeasure cannot be null");
        return unitMeasureRepository.findById(id).map(unitOfMeasureMapper::toDto);
    }

    @Override
    public UnitOfMeasureResponseDto save(UnitOfMeasureRequest unitOfMeasureRequest) {
        Objects.requireNonNull(unitOfMeasureRequest, "The Request provided for the Unit Of Measure cannot be null.");

        UnitOfMeasure unitOfMeasure = unitOfMeasureMapper.toEntity(unitOfMeasureRequest);
        validationsToSave(unitOfMeasure);
        return unitOfMeasureMapper.toDto(unitMeasureRepository.save(unitOfMeasure));
    }

    @Transactional
    @Override
    public Optional<UnitOfMeasureResponseDto> update(UUID id, UnitOfMeasureRequest unitOfMeasureRequest) {

        Objects.requireNonNull(id, "The ID for updating the Unit Of Measure cannot be null.");
        Objects.requireNonNull(unitOfMeasureRequest, "The details of the Unit Of Measure to be updated cannot be null.");

        UnitOfMeasure unitOfMeasure = unitOfMeasureMapper.toEntity(unitOfMeasureRequest);
        validateEntity(unitOfMeasure);

        return unitMeasureRepository.findById(id).map(unitMeasureRecovered -> {
            unitMeasureRecovered.setUnitName(unitOfMeasure.getUnitName());
            unitMeasureRecovered.setUnitType(unitOfMeasure.getUnitType());
            unitMeasureRecovered.setAbbreviation(unitOfMeasure.getAbbreviation());

            return unitOfMeasureMapper.toDto(unitMeasureRepository.save(unitMeasureRecovered));
        });
    }

    @Transactional
    @Override
    public void deleteById(UUID id) {
        Objects.requireNonNull(id, "The ID to be deleted cannot be null.");

        UnitOfMeasure unitMeasure = unitMeasureRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("The Unit Of Measure with ID %s does not exist.", id)));

        unitMeasure.setStatus(EntityStatus.DELETED);
        unitMeasureRepository.save(unitMeasure);
    }

    private void validateEntity(UnitOfMeasure unitOfMeasure) {
        Set<ConstraintViolation<UnitOfMeasure>> violations = validator.validate(unitOfMeasure);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void validationsToSave(UnitOfMeasure unitOfMeasure){
        validateEntity(unitOfMeasure);
        if (unitOfMeasure.getId() != null) {
            throw new IllegalArgumentException("You cannot save a Unit of Measure that already has an assigned ID. Use the update method instead.");
        }

        if (unitMeasureRepository.existsByUnitName(unitOfMeasure.getUnitName().trim())) {
            throw new EntityExistsException(String.format("The Unit Of Measure with name '%s' already exists", unitOfMeasure.getUnitName()));
        }
    }
}
