package com.humane.etms.controller.api;

import com.humane.etms.model.AttendMap;
import com.humane.etms.model.QAttendMap;
import com.humane.etms.repository.AttendMapRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "api/attendMap", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AttendMapController {
    private final AttendMapRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AttendMap> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Iterable<AttendMap> list(@QuerydslPredicate Predicate predicate, Sort sort) {
        return repository.findAll(predicate, sort);
    }

    @RequestMapping(value = "hall", method = RequestMethod.GET)
    public ResponseEntity<?> findByHall(@RequestParam(defaultValue = "") String attendCd, @RequestParam(defaultValue = "") String hallCd) {
        if (StringUtils.isAnyEmpty(attendCd, hallCd))
            return new ResponseEntity<>("parameters empty!", HttpStatus.BAD_REQUEST);

        QAttendMap attendMap = QAttendMap.attendMap;
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(attendMap.attend.attendCd.eq(attendCd));
        predicate.and(attendMap.hall.hallCd.eq(hallCd).or(attendMap.attendHall.hallCd.eq(hallCd)));

        return ResponseEntity.ok(repository.findAll(predicate));
    }

    @RequestMapping(value = "find", method = RequestMethod.GET)
    public ResponseEntity<?> findByExaminee(@RequestParam(defaultValue = "") String admissionCd, @RequestParam(defaultValue = "") String attendDate, @RequestParam(defaultValue = "") String examineeCd, @RequestParam(defaultValue = "") String examineeNm) throws ParseException {
        if (StringUtils.isEmpty(admissionCd) || (StringUtils.isEmpty(examineeCd) && StringUtils.isEmpty(examineeNm)))
            return new ResponseEntity<>("parameters empty!", HttpStatus.BAD_REQUEST);

        QAttendMap attendMap = QAttendMap.attendMap;
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(attendMap.attend.admission.admissionCd.eq(admissionCd));
        if (StringUtils.isNotEmpty(attendDate))
            predicate.and(attendMap.attend.attendDate.eq(new SimpleDateFormat("yyyy-MM-dd").parse(attendDate)));
        if (StringUtils.isNotEmpty(examineeCd))
            predicate.and(attendMap.examinee.examineeCd.like(examineeCd.concat("%")));
        if (StringUtils.isNotEmpty(examineeNm))
            predicate.and(attendMap.examinee.examineeNm.like(examineeNm.concat("%")));

        return ResponseEntity.ok(repository.findAll(predicate));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AttendMap> merge(@RequestBody AttendMap attendMap) { // 출결
        return new ResponseEntity<>(save(attendMap), HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendMap>> merge(@RequestBody Iterable<AttendMap> attendMaps) {
        ArrayList<AttendMap> rtn = new ArrayList<>();
        attendMaps.forEach(attendMap -> rtn.add(save(attendMap)));

        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    private AttendMap save(AttendMap attendMap) {
        // 기존 여부 확인
        AttendMap find = repository.findOne(new BooleanBuilder()
                .and(QAttendMap.attendMap.attend.eq(attendMap.getAttend()))
                .and(QAttendMap.attendMap.examinee.eq(attendMap.getExaminee()))
        );
        if (find != null) attendMap.set_id(find.get_id());

        return repository.save(attendMap);
    }
}