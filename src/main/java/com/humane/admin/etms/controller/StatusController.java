package com.humane.admin.etms.controller;

import com.humane.admin.etms.dto.StatusDto;
import com.humane.admin.etms.service.ApiService;
import com.humane.admin.etms.helper.ChartJsExportHelper;
import com.humane.util.ObjectConvert;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.jqgrid.JqgridPager;
import com.humane.util.spring.PageResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import rx.Observable;

import java.util.List;

@RestController
@RequestMapping(value = "status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StatusController {
    private static final String CHART = "chart";

    private final ApiService apiService;

    @RequestMapping(value = "attend/list")
    public ResponseEntity attend(StatusDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<StatusDto>>> observable = apiService.attend(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort());

        Response<PageResponse<StatusDto>> response = observable.toBlocking().first();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body()));
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "attend/{format:chart|pdf|xls|xlsx}")
    public ResponseEntity attend(@PathVariable String format, StatusDto statusDto, JqgridPager pager) {
        Observable<List<StatusDto>> observable = apiService.attend(
                ObjectConvert.asMap(statusDto)
                , pager.getSort());

        List<StatusDto> list = observable.toBlocking().first();

        switch (format) {
            case CHART:
                return ChartJsExportHelper.toResponseEntity("typeNm", list);
            default:
                return JasperReportsExportHelper.toResponseEntity("jrxml/status-attend.jrxml", format, list);
        }
    }

    @RequestMapping(value = "dept/list")
    public ResponseEntity dept(StatusDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<StatusDto>>> observable = apiService.dept(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort());

        Response<PageResponse<StatusDto>> response = observable.toBlocking().first();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body()));
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "dept/{format:chart|pdf|xls|xlsx}")
    public ResponseEntity dept(@PathVariable String format, StatusDto statusDto, JqgridPager pager) {
        Observable<List<StatusDto>> observable = apiService.dept(
                ObjectConvert.asMap(statusDto),
                pager.getSort()
        );
        List<StatusDto> list = observable.toBlocking().first();

        switch (format) {
            case CHART:
                return ChartJsExportHelper.toResponseEntity("deptNm", list);
            default:
                return JasperReportsExportHelper.toResponseEntity("jrxml/status-dept.jrxml", format, list);
        }
    }

    @RequestMapping(value = "hall/list")
    public ResponseEntity hall(StatusDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<StatusDto>>> observable = apiService.hall(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort());

        Response<PageResponse<StatusDto>> response = observable.toBlocking().first();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body()));
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "hall/{format:chart|pdf|xls|xlsx}")
    public ResponseEntity hall(@PathVariable String format, StatusDto statusDto, JqgridPager pager) {
        Observable<List<StatusDto>> observable = apiService.hall(
                ObjectConvert.asMap(statusDto),
                pager.getSort()
        );
        List<StatusDto> list = observable.toBlocking().first();

        switch (format) {
            case CHART:
                return ChartJsExportHelper.toResponseEntity("hallNm", list);
            default:
                return JasperReportsExportHelper.toResponseEntity("jrxml/status-hall.jrxml", format, list);
        }
    }

    @RequestMapping(value = "group/list")
    public ResponseEntity group(StatusDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<StatusDto>>> observable = apiService.group(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort());

        Response<PageResponse<StatusDto>> response = observable.toBlocking().first();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body()));
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "group/{format:chart|pdf|xls|xlsx}")
    public ResponseEntity group(@PathVariable String format, StatusDto statusDto, JqgridPager pager) {
        Observable<List<StatusDto>> observable = apiService.group(
                ObjectConvert.asMap(statusDto),
                pager.getSort()
        );
        List<StatusDto> list = observable.toBlocking().first();

        switch (format) {
            case CHART:
                return ChartJsExportHelper.toResponseEntity("groupNm", list);
            default:
                return JasperReportsExportHelper.toResponseEntity("jrxml/status-group.jrxml", format, list);
        }
    }
}