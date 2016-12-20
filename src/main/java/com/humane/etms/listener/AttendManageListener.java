package com.humane.etms.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.etms.model.AttendManage;
import com.humane.etms.model.AttendManageLog;
import com.humane.etms.repository.AttendManageLogRepository;
import com.humane.util.AutowireHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PreUpdate;

public class AttendManageListener {
    @Autowired private AttendManageLogRepository logRepository;
    @Autowired private ObjectMapper objectMapper;

    @PostPersist
    @PostUpdate
    public void logPreUpdate(AttendManage obj) {
        AutowireHelper.autowire(this, logRepository, objectMapper);
        AttendManageLog objLog = objectMapper.convertValue(obj, AttendManageLog.class);
        objLog.set_id(null);

        logRepository.save(objLog);
    }
}
