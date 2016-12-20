package com.humane.etms.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.etms.model.AttendMap;
import com.humane.etms.model.AttendMapLog;
import com.humane.etms.repository.AttendMapLogRepository;
import com.humane.util.AutowireHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostUpdate;

public class AttendMapListener {
    @Autowired private AttendMapLogRepository logRepository;
    @Autowired private ObjectMapper objectMapper;

    @PostUpdate
    public void logPreUpdate(AttendMap obj) {
        AutowireHelper.autowire(this, logRepository, objectMapper);
        AttendMapLog objLog = objectMapper.convertValue(obj, AttendMapLog.class);
        objLog.set_id(null);

        logRepository.save(objLog);
    }
}
