package com.humane.etms.controller.api;

import com.humane.etms.model.Attend;
import com.humane.etms.repository.AttendRepository;
import com.querydsl.core.types.Predicate;
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
@RequestMapping(value = "api/attend", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendController {
    private final AttendRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<Attend> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Attend> merge(@RequestBody Attend attend) {
        Attend rtn = repository.save(attend);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<Attend>> merge(@RequestBody Iterable<Attend> attends) {
        Iterable<Attend> rtn = repository.save(attends);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }
}