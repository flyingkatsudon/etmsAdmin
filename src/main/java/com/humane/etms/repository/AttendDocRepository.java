package com.humane.etms.repository;

import com.humane.etms.model.AttendDoc;
import com.humane.etms.model.AttendHall;
import com.humane.util.spring.data.QueryDslJpaExtendRepository;

public interface AttendDocRepository extends QueryDslJpaExtendRepository<AttendDoc, Long> {
}