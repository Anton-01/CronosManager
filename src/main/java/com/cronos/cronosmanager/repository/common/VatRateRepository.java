package com.cronos.cronosmanager.repository.common;

import com.cronos.cronosmanager.model.common.VatRate;
import com.cronos.cronosmanager.repository.base.SoftDeletableRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface VatRateRepository extends SoftDeletableRepository<VatRate, UUID> {
}
