package com.humane.etms.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.etms.model.*;
import com.humane.etms.repository.AttendPaperLogRepository;
import com.humane.etms.repository.AttendPaperRepository;
import com.humane.etms.repository.DeviceRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.List;

@RestController
@RequestMapping(value = "api/attendPaper", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AttendPaperController {
    private final AttendPaperRepository repository;
    private final AttendPaperLogRepository logRepository;
    private final DeviceRepository deviceRepository;

    private final ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AttendPaper> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.ASC, "_id")));
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Iterable<AttendPaper> list(@QuerydslPredicate Predicate predicate) {
        return repository.findAll(predicate, new Sort(Sort.Direction.ASC, "_id"));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AttendPaper> merge(@RequestBody AttendPaper attendPaper
            , @RequestHeader(name = "uuid") String uuid
            , @RequestHeader(name = "packagename") String packageName
            , @RequestHeader(name = "versionname") String versionName) {
        return new ResponseEntity<>(save(uuid, packageName, versionName, attendPaper), HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendPaper>> merge(@RequestBody Iterable<AttendPaper> attendPapers
            , @RequestHeader(name = "uuid") String uuid
            , @RequestHeader(name = "packagename") String packageName
            , @RequestHeader(name = "versionname") String versionName) {
        List<AttendPaper> list = new ArrayList<>();
        attendPapers.forEach(attendPaper -> list.add(save(uuid, packageName, versionName, attendPaper)));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    private AttendPaper save(String uuid, String packageName, String versionName, AttendPaper attendPaper) {
        AttendPaper find = repository.findOne(new BooleanBuilder()
                .and(QAttendPaper.attendPaper.attend.attendCd.eq(attendPaper.getAttend().getAttendCd()))
                .and(QAttendPaper.attendPaper.examinee.examineeCd.eq(attendPaper.getExaminee().getExamineeCd()))
                .and(QAttendPaper.attendPaper.paperCd.eq(attendPaper.getPaperCd()))
        );

        if (find != null) attendPaper.set_id(find.get_id());

        // 헤더에 포함된 아래 세 개의 값으로
        // device 테이블의 _id 값을 찾아 저장함
        Device tmp = deviceRepository.findOne(new BooleanBuilder()
                .and(QDevice.device.versionName.eq(versionName))
                .and(QDevice.device.uuid.eq(uuid))
                .and(QDevice.device.packageName.eq(packageName))
        );

        Long deviceId = null;
        if (tmp != null) deviceId = tmp.getDeviceId();

        attendPaper.setDeviceId(deviceId);

        return repository.save(attendPaper);
    }

    @PreUpdate
    public void logPreUpdate(AttendPaper obj) {
        AttendPaperLog objLog = objectMapper.convertValue(obj, AttendPaperLog.class);
        objLog.set_id(null);
        log.debug("{}", objLog);

        logRepository.save(objLog);
    }
}