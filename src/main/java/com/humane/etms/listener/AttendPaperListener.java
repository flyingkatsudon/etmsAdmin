package com.humane.etms.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.etms.model.AttendPaper;
import com.humane.etms.model.AttendPaperLog;
import com.humane.etms.repository.AttendPaperLogRepository;
import com.humane.util.AutowireHelper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AttendPaperListener {
    @Autowired private AttendPaperLogRepository logRepository;
    @Autowired private ObjectMapper objectMapper;

    @PostUpdate
    @PostPersist
    public void logPreUpdate(AttendPaper obj) {
        AutowireHelper.autowire(this, logRepository, objectMapper);
        AttendPaperLog objLog = objectMapper.convertValue(obj, AttendPaperLog.class);
        objLog.set_id(null);

        logRepository.save(objLog);
    }
}
