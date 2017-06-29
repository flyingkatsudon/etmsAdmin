package com.humane.etms.repository;

import com.humane.etms.model.UserAdmission;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAdmissionRepository extends QueryDslJpaExtendRepository<UserAdmission, String> {
}