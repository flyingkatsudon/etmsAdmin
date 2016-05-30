package com.humane.etms.repository;

import com.humane.etms.model.Admission;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRepository extends QueryDslJpaExtendRepository<Admission, String> {
}