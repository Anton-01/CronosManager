package com.cronos.cronosmanager.repository.common;

import com.cronos.cronosmanager.model.common.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
}
