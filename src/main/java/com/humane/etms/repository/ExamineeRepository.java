package com.humane.etms.repository;

import com.humane.etms.model.Examinee;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamineeRepository extends QueryDslJpaExtendRepository<Examinee, String> {

}