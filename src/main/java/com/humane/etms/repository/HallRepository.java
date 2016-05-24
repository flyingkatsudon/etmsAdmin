package com.humane.etms.repository;

import com.humane.etms.model.Hall;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;

public interface HallRepository extends QueryDslJpaExtendRepository<Hall, String> {
}