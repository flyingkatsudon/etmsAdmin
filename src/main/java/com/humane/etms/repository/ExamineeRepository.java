package com.humane.etms.repository;

import com.humane.etms.model.Examinee;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;

public interface ExamineeRepository extends QueryDslJpaExtendRepository<Examinee, String> {
}