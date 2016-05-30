package com.humane.etms.repository;

import com.humane.etms.model.Hall;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends QueryDslJpaExtendRepository<Hall, String> {
}