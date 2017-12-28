package com.humane.etms.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.etms.model.*;
import com.humane.etms.repository.AttendManageLogRepository;
import com.humane.etms.repository.AttendManageRepository;
import com.humane.etms.repository.DeviceRepository;
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
import org.springframework.web.bind.annotation.*;

import javax.persistence.PreUpdate;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "api/attendManage", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AttendManageController {
    private final AttendManageRepository repository;
    private final AttendManageLogRepository logRepository;
    private final DeviceRepository deviceRepository;
    private final ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AttendManage> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AttendManage> merge(@RequestHeader("deviceno") String deviceNo, @RequestHeader("packagename") String packageName, @RequestHeader("uuid") String uuid, @RequestBody AttendManage attendManage) {

        Device device = new Device();
        device.setDeviceNo(deviceNo);
        device.setUuid(uuid);
        device.setPackageName(packageName);

        return new ResponseEntity<>(save(device, attendManage), HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Iterable<AttendManage> list(@QuerydslPredicate Predicate predicate, Sort sort) {
        return repository.findAll(predicate, sort);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendManage>> merge(@RequestHeader("deviceno") String deviceNo, @RequestHeader("packagename") String packageName, @RequestHeader("uuid") String uuid, @RequestBody Iterable<AttendManage> attendManages) {
        ArrayList<AttendManage> rtn = new ArrayList<>();

        Device device = new Device();
        device.setDeviceNo(deviceNo);
        device.setUuid(uuid);
        device.setPackageName(packageName);

        attendManages.forEach(attendManage -> rtn.add(save(device, attendManage)));
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    private AttendManage save(Device device, AttendManage attendManage) {

        Device tmp = deviceRepository.findOne(new BooleanBuilder()
                .and(QDevice.device.device.deviceNo.eq(device.getDeviceNo()))
                .and(QDevice.device.packageName.eq(device.getPackageName()))
                .and(QDevice.device.uuid.eq(device.getUuid()))
        );

        // 기존 여부 확인
        AttendManage find = repository.findOne(new BooleanBuilder()
                .and(QAttendManage.attendManage.attend.eq(attendManage.getAttend()))
                .and(QAttendManage.attendManage.examinee.eq(attendManage.getExaminee()))
        );

        if (find != null) {
            attendManage.set_id(find.get_id());
            attendManage.setIdCheckDttm(find.getIdCheckDttm());
        }

        attendManage.setDeviceId(tmp.getDeviceId());

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