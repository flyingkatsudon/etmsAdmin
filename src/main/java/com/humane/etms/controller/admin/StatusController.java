package com.humane.etms.controller.admin;

import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.StatusMapper;
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
@RequestMapping(value = "status")
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StatusController {

    private static final String JSON = "json";

    private final StatusMapper mapper;

    @RequestMapping(value = "home")
    public ResponseEntity home(StatusDto param, Pageable pageable){
        return ResponseEntity.ok(mapper.home(param, pageable));
    }

    @RequestMapping(value = "all")
    public ResponseEntity all(StatusDto param) {
        log.debug("{}", param);
        return ResponseEntity.ok(mapper.all(param));
    }

    @RequestMapping(value = "hallStat")
    public ResponseEntity hallStat(StatusDto param) {
        log.debug("{}", param);
        return ResponseEntity.ok(mapper.hallStat(param));
    }

    // 모든 컨트롤러가 같은 형태를 가짐, URL 마지막에 확장자를 붙이는 포맷으로 만듦
    @RequestMapping(value = "attend.{format:json|pdf|xls|xlsx}")
    public ResponseEntity attend(@PathVariable String format, StatusDto statusDto, Pageable pageable) {
        // 가령, /status/attend.json의 URL을 타고 들어온다면
        switch (format) {
            // .json 이면 데이터만 page 타입으로 가져온다
            case JSON:
                return ResponseEntity.ok(mapper.attend(statusDto, pageable));
            // 그렇지 않으면 .xlsx 파일을 출력한다
            // jrxml은 jasper studio로 외부에서 작성 후 jrxml 폴더에 위치시킨다
            default:
               return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-attend.jrxml",
                        format,
                        mapper.attend(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "dept.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity dept(@PathVariable String format, StatusDto statusDto, Pageable pageable) {

        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.dept(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-dept.jrxml",
                        format,
                        mapper.dept(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "major.{format:json|pdf|xls|xlsx}")
    public ResponseEntity major(@PathVariable String format, StatusDto statusDto, Pageable pageable) {

        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.major(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-major.jrxml",
                        format,
                        mapper.major(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "hall.{format:json|chart|pdf|xls|xlsx}")
    public ResponseEntity hall(@PathVariable String format, StatusDto statusDto, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.hall(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-hall.jrxml",
                        format,
                        mapper.hall(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "group.{format:json|pdf|xls|xlsx}")
    public ResponseEntity group(@PathVariable String format, StatusDto statusDto, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.group(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/status-group.jrxml",
                        format,
                        mapper.group(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }
}