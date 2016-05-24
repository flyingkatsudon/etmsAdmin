package com.humane.etms.repository;

import com.humane.etms.model.Attend;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;

public interface AttendRepository extends QueryDslJpaExtendRepository<Attend, String> {
}