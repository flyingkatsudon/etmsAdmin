package com.humane.etms.repository;

import com.humane.etms.model.Staff;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends QueryDslJpaExtendRepository<Staff, String> {

}