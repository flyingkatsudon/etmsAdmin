package com.humane.etms.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.etms.model.AttendPaper;
import com.humane.etms.model.AttendPaperLog;
import com.humane.etms.model.QAttendPaper;
import com.humane.etms.repository.AttendPaperLogRepository;
import com.humane.etms.repository.AttendPaperRepository;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AttendPaper> merge(@RequestBody AttendPaper attendPaper) {
        return new ResponseEntity<>(save(attendPaper), HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendPaper>> merge(@RequestBody Iterable<AttendPaper> attendPapers) {
        List<AttendPaper> list = new ArrayList<>();
        attendPapers.forEach(attendPaper -> list.add(save(attendPaper)));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    private AttendPaper save(AttendPaper attendPaper) {
        AttendPaper find = repository.findOne(new BooleanBuilder()
                .and(QAttendPaper.attendPaper.attend.attendCd.eq(attendPaper.getAttend().getAttendCd()))
                .and(QAttendPaper.attendPaper.examinee.examineeCd.eq(attendPaper.getExaminee().getExamineeCd()))
                .and(QAttendPaper.attendPaper.paperCd.eq(attendPaper.getPaperCd()))
        );

        if (find != null) attendPaper.set_id(find.get_id());

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