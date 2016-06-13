package com.humane.etms.controller.admin;

import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.StatusMapper;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "status")
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StatusController {

    private static final String CHART = "chart";
    private static final String JSON = "json";

    private final StatusMapper mapper;

    @RequestMapping(value = "all")
    public ResponseEntity all(StatusDto statusDto) {
        return ResponseEntity.ok(mapper.all(statusDto));
    }

    @RequestMapping(value = "attend.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity attend(@PathVariable String format, StatusDto statusDto, Pageable pageable) {

        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.attend(statusDto, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.attend(statusDto, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-attend.jrxml",
                        format,
                        mapper.attend(statusDto, pageable).getContent()
                );
        }
    }

    @RequestMapping(value = "dept.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity dept(@PathVariable String format, StatusDto statusDto, Pageable pageable) {

        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.dept(statusDto, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.dept(statusDto, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-dept.jrxml",
                        format,
                        mapper.dept(statusDto, pageable).getContent()
                );
        }
    }

    @RequestMapping(value = "major.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity major(@PathVariable String format, StatusDto statusDto, Pageable pageable) {

        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.major(statusDto, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.major(statusDto, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-major.jrxml",
                        format,
                        mapper.major(statusDto, pageable).getContent()
                );
        }
    }

    @RequestMapping(value = "hall.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity hall(@PathVariable String format, StatusDto statusDto, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.hall(statusDto, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.hall(statusDto, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-hall.jrxml",
                        format,
                        mapper.hall(statusDto, pageable).getContent()
                );
        }
    }

    @RequestMapping(value = "group.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity group(@PathVariable String format, StatusDto statusDto, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.group(statusDto, pageable));
            case CHART:
                return ResponseEntity.ok(mapper.group(statusDto, pageable).getContent());
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-group.jrxml",
                        format,
                        mapper.group(statusDto, pageable).getContent()
                );
        }
    }
}