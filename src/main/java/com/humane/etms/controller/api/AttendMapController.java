package com.humane.etms.controller.api;

import com.humane.etms.model.AttendMap;
import com.humane.etms.model.QAttendMap;
import com.humane.etms.repository.AttendMapRepository;
import com.humane.util.spring.data.JoinDescriptor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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

    @RequestMapping(value = "find", method = RequestMethod.GET)
    public ResponseEntity<?> findByExaminee(@RequestParam(defaultValue = "") String examineeCd, @RequestParam(defaultValue = "") String examineeNm) {
        if (StringUtils.isEmpty(examineeCd) && StringUtils.isEmpty(examineeNm)) {
            return new ResponseEntity<>("parameters empty!", HttpStatus.BAD_REQUEST);
        }

        QAttendMap attendMap = QAttendMap.attendMap;
        BooleanBuilder predicate = new BooleanBuilder();
        if (StringUtils.isNotEmpty(examineeCd))
            predicate.and(attendMap.examinee.examineeCd.like(examineeCd.concat("%")));
        if (StringUtils.isNotEmpty(examineeNm)) predicate.and(attendMap.examinee.examineeNm.eq(examineeNm));

        return ResponseEntity.ok(repository.findAll(predicate));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AttendMap> merge(@RequestBody AttendMap attendMap) { // 출결
        QAttendMap qAttendMap = QAttendMap.attendMap;
        // 기존 여부 확인
        AttendMap find = repository.findOne(new BooleanBuilder()
                .and(qAttendMap.attend.eq(attendMap.getAttend()))
                .and(qAttendMap.examinee.eq(attendMap.getExaminee()))
                .and(qAttendMap.hall.eq(attendMap.getHall()))
        );
        AttendMap rtn = null;
        if (find != null) {
            find.setAttendHall(attendMap.getAttendHall()); // 출결고사장
            find.setAttendDttm(attendMap.getAttendDttm()); // 출결시간
            find.setIsCheck(attendMap.getIsCheck()); // 신원재확인 대상자
            find.setIsMidOut(attendMap.getIsMidOut()); // 중도퇴실자
            find.setIsCheat(attendMap.getIsCheat()); // 부정행위 대상자
            find.setMemo(attendMap.getMemo()); // 메모
            rtn = repository.save(find);
        }

        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendMap>> merge(@RequestBody Iterable<AttendMap> attendMaps) {
        QAttendMap qAttendMap = QAttendMap.attendMap;
        // 채점자는 채점자의 내용만 손대야 한다.
        ArrayList<AttendMap> rtn = new ArrayList<>();
        attendMaps.forEach(attendMap -> {
            AttendMap find = repository.findOne(new BooleanBuilder()
                    .and(qAttendMap.attend.eq(attendMap.getAttend()))
                    .and(qAttendMap.examinee.eq(attendMap.getExaminee()))
                    .and(qAttendMap.hall.eq(attendMap.getHall()))
            );
            if (find != null) {
                find.setAttendHall(attendMap.getAttendHall()); // 출결고사장
                find.setAttendDttm(attendMap.getAttendDttm()); // 출결시간
                find.setIsCheck(attendMap.getIsCheck()); // 신원재확인 대상자
                find.setIsMidOut(attendMap.getIsMidOut()); // 중도퇴실자
                find.setIsCheat(attendMap.getIsCheat()); // 부정행위 대상자
                find.setMemo(attendMap.getMemo()); // 메모

                rtn.add(repository.save(find));
            }
        });

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
            find.setIsNoIdCard(attendMap.getIsNoIdCard()); // 신분증 미소지자
            rtn = repository.save(find);
        }

        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "mgr/list", method = RequestMethod.POST)
    public ResponseEntity mgrList(@RequestBody Iterable<AttendMap> attendMaps) {
        // 중간관리자는 중간관리자의 내용만 손대야한다.
        QAttendMap qAttendMap = QAttendMap.attendMap;
        ArrayList<AttendMap> rtn = new ArrayList<>();

        attendMaps.forEach(attendMap -> {
            AttendMap find = repository.findOne(new BooleanBuilder()
                    .and(qAttendMap.attend.eq(attendMap.getAttend()))
                    .and(qAttendMap.examinee.eq(attendMap.getExaminee()))
                    .and(qAttendMap.hall.eq(attendMap.getHall()))
            );
            if (find != null) {
                find.setIsNoIdCard(attendMap.getIsNoIdCard());

                rtn.add(repository.save(find));
            }
        });
        return ResponseEntity.ok(rtn);
    }
}