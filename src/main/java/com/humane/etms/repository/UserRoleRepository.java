package com.humane.etms.repository;

import com.humane.etms.model.UserRole;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends QueryDslJpaExtendRepository<UserRole, String> {
}