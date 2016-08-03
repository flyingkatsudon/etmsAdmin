package com.humane.etms.controller.api;

import com.humane.etms.model.AttendHall;
import com.humane.etms.model.QAttendHall;
import com.humane.etms.repository.AttendHallRepository;
import com.humane.util.spring.data.JoinDescriptor;
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
@RequestMapping(value = "api/attendHall", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendHallController {
    private final AttendHallRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AttendHall> index(@QuerydslPredicate Predicate predicate, @PageableDefault Pageable pageable) {
        QAttendHall attendHall = QAttendHall.attendHall;
        return repository.findAll(
                predicate,
                pageable,
                JoinDescriptor.innerJoin(attendHall.hall),
                JoinDescriptor.innerJoin(attendHall.attend)
        );
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<AttendHall> merge(@RequestBody AttendHall attend) {
        AttendHall rtn = repository.save(attend);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ResponseEntity<Iterable<AttendHall>> merge(@RequestBody Iterable<AttendHall> attends) {
        Iterable<AttendHall> rtn = repository.save(attends);
        return new ResponseEntity<>(rtn, HttpStatus.OK);
    }
}