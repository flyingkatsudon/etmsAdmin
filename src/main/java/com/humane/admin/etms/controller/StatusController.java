package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.RestApi;
import com.humane.admin.etms.dto.ChartJsDto;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.admin.etms.service.ExportService;
import com.humane.util.ObjectConvert;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.jqgrid.JqgridPager;
import com.humane.util.jqgrid.JqgridResponse;
import com.humane.util.spring.PageResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import retrofit2.Response;
import rx.Observable;

import java.util.List;

@RestController
@RequestMapping(value = "status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StatusController {
    private static final String LIST = "list";
    private static final String CHART = "chart";
    private static final String PDF = "pdf";
    private static final String XLSX = "xlsx";
    private static final String XLS = "xls";

    private final RestApi restApi;
    private final ExportService exportService;

    @RequestMapping(value = "attend/{path:list|chart|xls|xlsx}")
    public Object attend(@PathVariable String path, StatusDto statusDto, JqgridPager pager) {

        Observable<Response<PageResponse<StatusDto>>> observable = restApi.attend(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort()
        );

        PageResponse<StatusDto> page = observable.toBlocking().first().body();

        switch (path) {
            case LIST:
                return ResponseEntity.ok(JqgridMapper.getResponse(page));
            case CHART:
                return ResponseEntity.ok(toChart("typeNm", page.getContent()));
            case PDF:
            case XLS:
            case XLSX:
                ModelMap modelMap = new ModelMap();
                modelMap.put("format", path);
                modelMap.put("datasource", page.getContent());
                return new ModelAndView("status-attend.jrxml", modelMap);
        }

        return null;
/*

        switch (path) {
            case LIST:
                return exportService.toJqgrid(observable);
            case CHART:
                return exportService.toChart(observable, "typeNm");
            default:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }*/
    }

    @RequestMapping(value = "dept/{path}")
    public ResponseEntity dept(@PathVariable String path, StatusDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.dept(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort()
        );

        switch (path) {
            case LIST:
                return exportService.toJqgrid(observable);
            case CHART:
                return exportService.toChart(observable, "deptNm");
            default:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(value = "hall/{path}")
    public ResponseEntity hall(@PathVariable String path, StatusDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.hall(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort()
        );

        switch (path) {
            case LIST:
                return exportService.toJqgrid(observable);
            case CHART:
                return exportService.toChart(observable, "hallNm");
            default:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @RequestMapping(value = "group/{path}")
    public ResponseEntity group(@PathVariable String path, StatusDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.group(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort()
        );

        switch (path) {
            case LIST:
                return exportService.toJqgrid(observable);
            case CHART:
                return exportService.toChart(observable, "groupNm");
            default:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private ChartJsDto toChart(String fieldName, List<StatusDto> content) {
        ChartJsDto chartJsDto = new ChartJsDto();
        ChartJsDto.Dataset attendCnt = new ChartJsDto.Dataset("응시자수");
        ChartJsDto.Dataset attendPer = new ChartJsDto.Dataset("응시율");
        ChartJsDto.Dataset absentCnt = new ChartJsDto.Dataset("결시자수");
        ChartJsDto.Dataset absentPer = new ChartJsDto.Dataset("결시율");
        content.forEach(statusDto -> {
            try {
                chartJsDto.addLabel(FieldUtils.readField(statusDto, fieldName, true).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            attendCnt.addData(statusDto.getAttendCnt() == null ? 0 : statusDto.getAttendCnt());
            attendPer.addData(statusDto.getAttendPer() == null ? 0 : statusDto.getAttendPer());
            absentCnt.addData(statusDto.getAbsentCnt() == null ? 0 : statusDto.getAbsentCnt());
            absentPer.addData(statusDto.getAbsentPer() == null ? 0 : statusDto.getAbsentPer());
        });
        chartJsDto.addDataset(attendCnt);
        chartJsDto.addDataset(attendPer);
        chartJsDto.addDataset(absentCnt);
        chartJsDto.addDataset(absentPer);

        return chartJsDto;
    }
}