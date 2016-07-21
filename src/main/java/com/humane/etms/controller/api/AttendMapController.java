package com.humane.etms.controller.api;

import com.humane.etms.model.AttendMap;
import com.humane.etms.model.QAttendMap;
import com.humane.etms.repository.AttendMapRepository;
import com.humane.util.spring.data.JoinDescriptor;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/attendMap", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendMapController {
    private final AttendMapRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AttendMap> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        QAttendMap attendMap = QAttendMap.attendMap;
        return repository.findAll(
                predicate,
                pageable,
                JoinDescriptor.innerJoin(attendMap.examinee),
                JoinDescriptor.innerJoin(attendMap.hall),
                JoinDescriptor.innerJoin(attendMap.attend)
        );
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AttendMap> merge(@RequestBody AttendMap attendMap) {
        // 채점자는 채점자의 내용만 손대야 한다.
        QAttendMap qAttendMap = QAttendMap.attendMap;
        AttendMap find = repository.findOne(new BooleanBuilder()
                .and(qAttendMap.attend.eq(attendMap.getAttend()))
                .and(qAttendMap.examinee.eq(attendMap.getExaminee()))
                .and(qAttendMap.hall.eq(attendMap.getHall()))
        );
        AttendMap rtn = null;
        if (find != null) {
            attendMap.setIsNoIdCard(find.getIsNoIdCard());
            rtn = repository.save(attendMap);
        }

        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "mgr", method = RequestMethod.POST)
    public ResponseEntity<AttendMap> mgr(@RequestBody AttendMap attendMap) {
        // 중간관리자는 중간관리자의 내용만 손대야한다.
        QAttendMap qAttendMap = QAttendMap.attendMap;
        AttendMap find = repository.findOne(new BooleanBuilder()
                .and(qAttendMap.attend.eq(attendMap.getAttend()))
                .and(qAttendMap.examinee.eq(attendMap.getExaminee()))
                .and(qAttendMap.hall.eq(attendMap.getHall()))
        );
        AttendMap rtn = null;
        if (find != null) {
            find.setIsNoIdCard(attendMap.getIsNoIdCard());
            rtn = repository.save(find);
        }

        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "mgr/list", method = RequestMethod.POST)
    public ResponseEntity mgrList(@RequestBody Iterable<AttendMap> attendMaps) {
        // 중간관리자는 중간관리자의 내용만 손대야한다.
        QAttendMap qAttendMap = QAttendMap.attendMap;
        attendMaps.forEach(attendMap -> {
            AttendMap find = repository.findOne(new BooleanBuilder()
                    .and(qAttendMap.attend.eq(attendMap.getAttend()))
                    .and(qAttendMap.examinee.eq(attendMap.getExaminee()))
                    .and(qAttendMap.hall.eq(attendMap.getHall()))
            );
            if (find != null) {
                find.setIsNoIdCard(attendMap.getIsNoIdCard());
                repository.save(find);
            }
        });
        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendMap>> merge(@RequestBody Iterable<AttendMap> attendMaps) {
        Iterable<AttendMap> rtn = repository.save(attendMaps);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }
}