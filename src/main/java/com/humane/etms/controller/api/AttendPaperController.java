package com.humane.etms.controller.api;

import com.humane.etms.model.AttendPaper;
import com.humane.etms.model.AttendPaperLog;
import com.humane.etms.model.QAttendPaper;
import com.humane.etms.model.QAttendPaperLog;
import com.humane.etms.repository.AttendPaperLogRepository;
import com.humane.etms.repository.AttendPaperRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/attendPaper", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendPaperController {
    private final AttendPaperRepository repository;
    private final AttendPaperLogRepository logRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AttendPaper> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort(Sort.Direction.ASC, "_id")));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AttendPaper> merge(@RequestBody AttendPaper attendPaper) {
        QAttendPaper qAttendPaper = QAttendPaper.attendPaper;

        AttendPaper find = repository.findOne(new BooleanBuilder()
                .and(qAttendPaper.attend.attendCd.eq(attendPaper.getAttend().getAttendCd()))
                .and(qAttendPaper.paperCd.eq(attendPaper.getPaperCd()))
        );

        if (find != null) attendPaper.set_id(find.get_id());

        AttendPaper rtn = repository.save(attendPaper);
        saveLog(rtn);

        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    /*@RequestMapping(value = "old", method = RequestMethod.POST)
    public ResponseEntity<AttendPaper> mergeOld(@RequestBody AttendPaper attendPaper) {
        QAttendPaper qAttendPaper = QAttendPaper.attendPaper;

        AttendPaper find = repository.findOne(new BooleanBuilder()
                .and(qAttendPaper.attend.attendCd.eq(attendPaper.getAttend().getAttendCd()))
                .and(qAttendPaper.oldPaperCd.eq(attendPaper.getOldPaperCd()))
        );

        if (find != null) attendPaper.set_id(find.get_id());

        AttendPaper rtn = repository.save(attendPaper);
        saveLog(rtn);

        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }*/

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendPaper>> merge(@RequestBody Iterable<AttendPaper> attendPapers) {
        QAttendPaper qAttendPaper = QAttendPaper.attendPaper;

        List<AttendPaper> list = new ArrayList<>();

        attendPapers.forEach(attendPaper -> {
            AttendPaper find = repository.findOne(new BooleanBuilder()
                    .and(qAttendPaper.attend.attendCd.eq(attendPaper.getAttend().getAttendCd()))
                    .and(qAttendPaper.paperCd.eq(attendPaper.getPaperCd()))
            );

            if (find != null) attendPaper.set_id(find.get_id());

            list.add(repository.save(attendPaper));

            saveLog(attendPaper);
        });
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    private void saveLog(AttendPaper paper) {
        QAttendPaperLog paperLog = QAttendPaperLog.attendPaperLog;

        AttendPaperLog findLog = logRepository.findOne(new BooleanBuilder()
                .and(paperLog.attend.attendCd.eq(paper.getAttend().getAttendCd()))
                .and(paperLog.paperCd.eq(paper.getPaperCd()))
                .and(paperLog.paperNo.eq(paper.getPaperNo()))
                .and(paperLog.examinee.examineeCd.eq(paper.getExaminee().getExamineeCd()))
                .and(paperLog.hall.hallCd.eq(paper.getHall().getHallCd()))
                .and(paperLog.regDttm.eq(paper.getRegDttm()))
                .and(paper.getOldPaperCd() == null ? paperLog.oldPaperCd.isNull() : paperLog.oldPaperCd.eq(paper.getOldPaperCd()))
        );

        if (findLog == null) {
            AttendPaperLog log = new AttendPaperLog();
            log.setAttendPaper(paper);
            logRepository.save(log);
        }
    }
}