package com.cronos.cronosmanager.service.common.impl;

import com.cronos.cronosmanager.dto.common.impl.VatRateMapper;
import com.cronos.cronosmanager.dto.common.request.VatRateRequestDto;
import com.cronos.cronosmanager.dto.common.response.VatRateResponseDto;
import com.cronos.cronosmanager.enums.EntityStatus;
import com.cronos.cronosmanager.exception.common.ResourceNotFoundException;
import com.cronos.cronosmanager.model.common.VatRate;
import com.cronos.cronosmanager.repository.common.VatRateRepository;
import com.cronos.cronosmanager.service.common.VatRateService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VatRateServiceImpl implements VatRateService {

    private final VatRateRepository vatRateRepository;
    private final VatRateMapper vatRateMapper;
    private final Validator validator;

    @Transactional(readOnly = true)
    @Override
    public Page<VatRateResponseDto> findAll(Pageable pageable) {
        Page<VatRate> rates = vatRateRepository.findAll(pageable);
        return rates.map(vatRateMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<VatRateResponseDto> findById(UUID id) {
        Objects.requireNonNull(id, "Id Of Vat Rate cannot be null");
        return vatRateRepository.findById(id).map(vatRateMapper::toDto);
    }

    @Transactional
    @Override
    public VatRateResponseDto save(VatRateRequestDto rateRequestDto) {
        Objects.requireNonNull(rateRequestDto, "The Request provided for Vat Rate cannot be null.");

        VatRate vatRate = vatRateMapper.toEntity(rateRequestDto);
        validationsToSave(vatRate);
        return vatRateMapper.toDto(vatRateRepository.save(vatRate));
    }

    @Transactional
    @Override
    public Optional<VatRateResponseDto> update(UUID id, VatRateRequestDto requestDTO) {
        Objects.requireNonNull(id, "Id Of Vat Rate cannot be null");
        Objects.requireNonNull(requestDTO, "The Request provided for Vat Rate cannot be null.");

        Optional<VatRate> existingRateOpt = vatRateRepository.findById(id);
        if (existingRateOpt.isEmpty()) {
            return Optional.empty();
        }

        VatRate existingRate = existingRateOpt.get();
        validateEntity(existingRate);
        mapperFieldsToUpdate(existingRate, requestDTO);

        return Optional.of(vatRateMapper.toDto(vatRateRepository.save(existingRate)));
    }

    @Transactional
    @Override
    public void deleteById(UUID id) {
        Objects.requireNonNull(id, "The ID to be deleted cannot be null.");

        VatRate vatRate = vatRateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("The Vat Rate with ID %s does not exist.", id)));

        vatRate.setStatus(EntityStatus.DELETED);
        vatRateRepository.save(vatRate);
    }

    private void validationsToSave(VatRate vatRate){
        validateEntity(vatRate);
        if (vatRate.getId() != null) {
            throw new IllegalArgumentException("You cannot save a Vat Rate that already has an assigned ID. Use the update method instead.");
        }
    }

    private void validateEntity(VatRate vatRate) {
        Set<ConstraintViolation<VatRate>> violations = validator.validate(vatRate);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void mapperFieldsToUpdate(VatRate vatRate, VatRateRequestDto requestDTO) {
        vatRate.setName(requestDTO.getName());
        vatRate.setRate(requestDTO.getRate());
        vatRate.setIsDefault(requestDTO.getIsDefault());
        vatRate.setEffectiveDate(requestDTO.getEffectiveDate());
        vatRate.setEndDate(requestDTO.getEndDate());
    }
}
