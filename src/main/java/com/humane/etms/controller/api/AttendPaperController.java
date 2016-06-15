package com.humane.etms.controller.api;

import com.humane.etms.model.AttendPaper;
import com.humane.etms.repository.AttendPaperRepository;
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
        AttendPaper rtn = repository.save(attendPaper);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendPaper>> merge(@RequestBody Iterable<AttendPaper> attendPapers) {
        Iterable<AttendPaper> rtn = repository.save(attendPapers);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }
}