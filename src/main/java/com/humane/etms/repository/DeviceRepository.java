package com.humane.etms.repository;

import com.humane.etms.model.Device;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends QueryDslJpaExtendRepository<Device, Long> {
}