package com.humane.etms.controller.admin;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.CheckMapper;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "check")
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CheckController {
    private static final String JSON = "json";

    private final CheckMapper mapper;

    @RequestMapping(value = "paper.{format:json|xls|xlsx}")
    public ResponseEntity paper(@PathVariable String format, StatusDto statusDto, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.paper(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/check-paper.jrxml",
                        format,
                        mapper.paper(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "device.{format:json|xls|xlsx}")
    public ResponseEntity device(@PathVariable String format, StatusDto statusDto, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.device(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/check-device.jrxml",
                        format,
                        mapper.device(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "recheck.{format:json|pdf|xls|xlsx}")
    public ResponseEntity recheck(@PathVariable String format, ExamineeDto examineeDto, Pageable pageable) {
        examineeDto.setIsCheck(true);
        examineeDto.setIsNoIdCard(true);
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.recheck(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/check-recheck.jrxml",
                        format,
                        mapper.recheck(examineeDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "signature.{format:json|xls|xlsx}")
    public ResponseEntity signature(@PathVariable String format, StatusDto statusDto, Pageable pageable) {
        statusDto.setIsSignature(false);
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.signature(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/check-signature.jrxml",
                        format,
                        mapper.signature(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }
}