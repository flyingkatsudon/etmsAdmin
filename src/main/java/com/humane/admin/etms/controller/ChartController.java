package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import com.humane.admin.etms.dto.ChartJsDto;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.query.QueryBuilder;
import com.humane.util.spring.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import rx.Observable;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("chart")
@Slf4j
public class ChartController {

    @Autowired private ApiService apiService;

    @RequestMapping(value = "attend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity attend(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "1000") int rows
    ) throws IOException {
        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<Response<PageResponse<StatusDto>>> observable = apiService.statusAttend(query, page - 1, rows, sort);
        return toChart(observable, "typeNm");
    }

    @RequestMapping(value = "dept", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ChartJsDto> dept(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "1000") int rows
    ) throws IOException {

        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<Response<PageResponse<StatusDto>>> observable = apiService.statusDept(query, page - 1, rows, sort);
        return toChart(observable, "deptNm");
    }

    @RequestMapping(value = "hall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ChartJsDto> hall(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "1000") int rows
    ) throws IOException {

        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<Response<PageResponse<StatusDto>>> observable = apiService.statusHall(query, page - 1, rows, sort);
        return toChart(observable, "hallNm");
    }

    private ResponseEntity<ChartJsDto> toChart(Observable<Response<PageResponse<StatusDto>>> observable, String typeNm) {
        try {
            Response<PageResponse<StatusDto>> res = observable.toBlocking().first();
            if (res.isSuccessful()) return ResponseEntity.ok(toChart("typeNm", res.body().getContent()));
            else {
                log.error("{}", res.errorBody());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
            }
        } catch (Throwable e) {
            log.debug("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
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
