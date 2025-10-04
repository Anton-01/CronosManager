package com.cronos.cronosmanager.repository.common;

import com.cronos.cronosmanager.model.common.ProductType;
import com.cronos.cronosmanager.repository.base.SoftDeletableRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductTypeRepository extends SoftDeletableRepository<ProductType, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
