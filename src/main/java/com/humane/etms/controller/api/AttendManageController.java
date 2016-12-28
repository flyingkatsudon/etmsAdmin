package com.humane.etms.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.etms.model.*;
import com.humane.etms.repository.AttendManageLogRepository;
import com.humane.etms.repository.AttendManageRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.PreUpdate;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "api/attendManage", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AttendManageController {
    private final AttendManageRepository repository;
    private final AttendManageLogRepository logRepository;
    private final ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AttendManage> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AttendManage> merge(@RequestBody AttendManage attendManage) {
        return new ResponseEntity<>(save(attendManage), HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Iterable<AttendManage> list(@QuerydslPredicate Predicate predicate, Sort sort){
        return repository.findAll(predicate, sort);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendManage>> merge(@RequestBody Iterable<AttendManage> attendManages) {
        ArrayList<AttendManage> rtn = new ArrayList<>();
        attendManages.forEach(attendManage -> rtn.add(save(attendManage)));
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    private AttendManage save(AttendManage attendManage){
        // 기존 여부 확인
        AttendManage find = repository.findOne(new BooleanBuilder()
                .and(QAttendManage.attendManage.attend.eq(attendManage.getAttend()))
                .and(QAttendManage.attendManage.examinee.eq(attendManage.getExaminee()))
        );
        if (find != null) attendManage.set_id(find.get_id());

        return repository.save(attendManage);
    }

    @PreUpdate
    public void logPreUpdate(AttendManage obj) {
        AttendManageLog objLog = objectMapper.convertValue(obj, AttendManageLog.class);
        objLog.set_id(null);
        log.debug("{}", objLog);

        logRepository.save(objLog);
    }
}