package com.humane.etms.repository;

import com.humane.etms.model.User;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends QueryDslJpaExtendRepository<User, String> {
}