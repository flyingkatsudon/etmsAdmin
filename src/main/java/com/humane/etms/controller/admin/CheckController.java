package com.humane.etms.controller.admin;

import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.CheckMapper;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "check")
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CheckController {
    private static final String LIST = "list";

    private final CheckMapper mapper;

    @RequestMapping(value = "send/{format:list|xls|xlsx}")
    public ResponseEntity send(@PathVariable String format, StatusDto statusDto, Pageable pageable, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.send(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-send.jrxml",
                        format,
                        mapper.send(statusDto, pageable).getContent()
                );
        }
    }

    @RequestMapping(value = "device/{format:list|xls|xlsx}")
    public ResponseEntity device(@PathVariable String format, StatusDto statusDto, Pageable pageable, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.device(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-device.jrxml",
                        format,
                        mapper.device(statusDto, pageable).getContent()
                );
        }
    }

    @RequestMapping(value = "detect/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity detect(@PathVariable String format, StatusDto statusDto, Pageable pageable, HttpServletResponse response) {
        return null;
    }

    @RequestMapping(value = "signature/{format:list|xls|xlsx}")
    public ResponseEntity signature(@PathVariable String format, StatusDto statusDto, Pageable pageable, HttpServletResponse response) {
        statusDto.setIsSignature(false);
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.signature(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-signature.jrxml",
                        format,
                        mapper.signature(statusDto, pageable).getContent()
                );
        }
    }
}