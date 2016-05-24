package com.humane.etms.controller;

import com.humane.etms.model.AttendMap;
import com.humane.etms.model.QAttendMap;
import com.humane.etms.repository.AttendMapRepository;
import com.humane.util.spring.data.JoinDescriptor;
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
        AttendMap rtn = repository.save(attendMap);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendMap>> merge(@RequestBody Iterable<AttendMap> attendMaps) {
        Iterable<AttendMap> rtn = repository.save(attendMaps);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }
}