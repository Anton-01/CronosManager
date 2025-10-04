package com.cronos.cronosmanager.repository;

import com.cronos.cronosmanager.model.CronosConstantData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CronosConstantDataRepository extends JpaRepository<CronosConstantData, Long> {

    // Search active properties by specific profile
    List<CronosConstantData> findByProfileAndActive(String profile, boolean active);
}
