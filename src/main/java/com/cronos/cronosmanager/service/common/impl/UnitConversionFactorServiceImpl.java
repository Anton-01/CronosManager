package com.cronos.cronosmanager.service.common.impl;

import com.cronos.cronosmanager.dto.common.impl.UnitConversionFactorMapper;
import com.cronos.cronosmanager.dto.common.request.UnitConversionFactorRequestDto;
import com.cronos.cronosmanager.dto.common.response.UnitConversionFactorResponseDto;
import com.cronos.cronosmanager.enums.EntityStatus;
import com.cronos.cronosmanager.exception.common.ResourceNotFoundException;
import com.cronos.cronosmanager.model.common.UnitConversionFactor;
import com.cronos.cronosmanager.model.common.UnitOfMeasure;
import com.cronos.cronosmanager.repository.common.UnitConversionFactorRepository;
import com.cronos.cronosmanager.repository.common.UnitMeasureRepository;
import com.cronos.cronosmanager.service.common.UnitConversionFactorService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnitConversionFactorServiceImpl implements UnitConversionFactorService {

    private final UnitConversionFactorRepository conversionFactorRepository;
    private final UnitMeasureRepository unitOfMeasureRepository;
    private final UnitConversionFactorMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UnitConversionFactorResponseDto> findAll(Pageable pageable) {
        return conversionFactorRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UnitConversionFactorResponseDto> findById(UUID id) {
        return conversionFactorRepository.findById(id).map(mapper::toDto);
    }

    @Override
    @Transactional
    public UnitConversionFactorResponseDto save(UnitConversionFactorRequestDto requestDTO) {
        // Search base entities from id´s in dto
        UnitOfMeasure fromUnit = unitOfMeasureRepository.findById(requestDTO.getFromUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("From-Unit with ID " + requestDTO.getFromUnitId() + " not found."));

        UnitOfMeasure toUnit = unitOfMeasureRepository.findById(requestDTO.getToUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("To-Unit with ID " + requestDTO.getToUnitId() + " not found."));

        // Verify unit restriction before save for not duplicity
        if (conversionFactorRepository.existsByFromUnitAndToUnit(fromUnit, toUnit)) {
            throw new EntityExistsException("A conversion factor from '" + fromUnit.getUnitName() + "' to '" + toUnit.getUnitName() + "' already exists.");
        }

        // Verify compactibility between unit types
        if (!fromUnit.getUnitType().equals(toUnit.getUnitType())) {
            throw new IllegalArgumentException("Conversion units must be of the same type. Unit 1 is type '" + fromUnit.getUnitType()
                            + "', Unit 2 is type '" + toUnit.getUnitType() + "'."
            );
        }

        // Mapper fields
        UnitConversionFactor newFactor = mapper.toEntity(requestDTO);

        // Assign complete founded entities
        newFactor.setFromUnit(fromUnit);
        newFactor.setToUnit(toUnit);


        UnitConversionFactor savedFactor = conversionFactorRepository.save(newFactor);
        return mapper.toDto(savedFactor);
    }

    @Override
    @Transactional
    public Optional<UnitConversionFactorResponseDto> update(UUID id, UnitConversionFactorRequestDto requestDTO) {
        Optional<UnitConversionFactor> existingFactorOpt = conversionFactorRepository.findById(id);

        if (existingFactorOpt.isEmpty()) {
            return Optional.empty();
        }

        UnitConversionFactor factorToUpdate = existingFactorOpt.get();

        UnitOfMeasure fromUnit = unitOfMeasureRepository.findById(requestDTO.getFromUnitId()).orElseThrow(() -> new ResourceNotFoundException("From-Unit with ID " + requestDTO.getFromUnitId() + " not found."));

        UnitOfMeasure toUnit = unitOfMeasureRepository.findById(requestDTO.getToUnitId()).orElseThrow(() -> new ResourceNotFoundException("To-Unit with ID " + requestDTO.getToUnitId() + " not found."));

        if (!fromUnit.getUnitType().equals(toUnit.getUnitType())) {
            throw new IllegalArgumentException("Conversion units must be of the same type.");
        }

        // Verificamos si existe otra combinación igual, pero que no sea el registro que estamos actualizando.
        conversionFactorRepository.findByFromUnitAndToUnit(fromUnit, toUnit).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new IllegalArgumentException("This conversion factor combination already exists for another record.");
            }
        });

        // Update fields from entity founded
        factorToUpdate.setFromUnit(fromUnit);
        factorToUpdate.setToUnit(toUnit);
        factorToUpdate.setFactor(requestDTO.getFactor());

        UnitConversionFactor updatedFactor = conversionFactorRepository.save(factorToUpdate);
        return Optional.of(mapper.toDto(updatedFactor));
    }

    @Transactional
    @Override
    public void deleteById(UUID id) {
        Objects.requireNonNull(id, "The ID to be deleted cannot be null.");

        UnitConversionFactor unitMeasure = conversionFactorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("The Unit Conversion Factor with ID %s does not exist.", id)));

        unitMeasure.setStatus(EntityStatus.DELETED);
        conversionFactorRepository.save(unitMeasure);
    }
}
