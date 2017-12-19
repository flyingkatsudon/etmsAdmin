package com.humane.etms.controller.admin;

import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.CheckMapper;
import com.humane.etms.service.CheckService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping(value = "check")
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CheckController {
    private static final String JSON = "json";

    private final CheckMapper checkMapper;
    private final CheckService checkService;

    @RequestMapping(value = "paper.{format:json|xls|xlsx}")
    public ResponseEntity paper(@PathVariable String format, StatusDto statusDto, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(checkMapper.paper(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/check-paper.jrxml",
                        format,
                        checkMapper.paper(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "detect1")
    public ResponseEntity detect1(StatusDto statusDto, Pageable pageable) {
        return ResponseEntity.ok(checkMapper.detect1(statusDto, pageable));
    }

    @RequestMapping(value = "detect2")
    public ResponseEntity detect2(StatusDto statusDto, Pageable pageable) {
        return ResponseEntity.ok(checkMapper.detect2(statusDto, pageable));
    }

    @RequestMapping(value = "invalid")
    public ResponseEntity invalid(@RequestParam("way") String way, StatusDto statusDto, Pageable pageable) {
        return ResponseEntity.ok(checkMapper.invalid(way, statusDto, pageable));
    }

    @RequestMapping(value = "multiple")
    public ResponseEntity multiple(StatusDto statusDto, Pageable pageable) {
        log.debug("{}", statusDto.getAttendDate());
        log.debug("{}", statusDto.getAttendTime());
        return ResponseEntity.ok(checkMapper.multiple(statusDto, pageable));
    }

    @RequestMapping(value = "detail")
    public ResponseEntity detail(StatusDto statusDto, Pageable pageable) throws ParseException {
        return ResponseEntity.ok(checkService.getDetailLog(statusDto, pageable));
    }
}