package com.humane.etms.controller.api;

import com.humane.etms.model.AttendPaper;
import com.humane.etms.model.QAttendPaper;
import com.humane.etms.repository.AttendPaperRepository;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/attendPaper", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendPaperController {
    private final AttendPaperRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AttendPaper> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AttendPaper> merge(@RequestBody AttendPaper attendPaper) {
        QAttendPaper qAttendPaper = QAttendPaper.attendPaper;

        AttendPaper find = repository.findOne(new BooleanBuilder()
                .and(qAttendPaper.attend.attendCd.eq(attendPaper.getAttend().getAttendCd()))
                .and(qAttendPaper.paperCd.eq(attendPaper.getPaperCd()))
        );

        if (find != null) {
            attendPaper.set_id(find.get_id());
            if (find.getOldPaperCd() != null) attendPaper.setOldPaperCd(find.getOldPaperCd());
        }

        AttendPaper rtn = repository.save(attendPaper);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendPaper>> merge(@RequestBody Iterable<AttendPaper> attendPapers) {
        QAttendPaper qAttendPaper = QAttendPaper.attendPaper;

        List<AttendPaper> list = new ArrayList<>();

        attendPapers.forEach(attendPaper -> {
            AttendPaper find = repository.findOne(new BooleanBuilder()
                    .and(qAttendPaper.attend.attendCd.eq(attendPaper.getAttend().getAttendCd()))
                    .and(qAttendPaper.paperCd.eq(attendPaper.getPaperCd()))
            );

            if (find != null) {
                attendPaper.set_id(find.get_id());
                if (find.getOldPaperCd() != null) attendPaper.setOldPaperCd(find.getOldPaperCd());
            } else {
                attendPaper.set_id(null);
            }
            list.add(repository.save(attendPaper));
        });
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}