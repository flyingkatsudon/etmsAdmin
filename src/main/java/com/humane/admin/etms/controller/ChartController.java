package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import com.humane.admin.etms.dto.ChartJsDto;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.query.QueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.schedulers.Schedulers;

import java.io.IOException;

@RestController
@RequestMapping("chart")
@Slf4j
public class ChartController {

    private final ApiService apiService;

    @Autowired
    public ChartController(ApiService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping(value = "attend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<ChartJsDto> attend(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "1000") int rows
    ) throws IOException {

        DeferredResult<ChartJsDto> deferred = new DeferredResult<>();

        apiService.statusAttend(QueryBuilder.getQueryString(q), page - 1, rows, JqgridMapper.getSortString(sidx, sord))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(res -> {
                    if (res.isSuccessful()) {
                        ChartJsDto chartJsDto = new ChartJsDto();
                        ChartJsDto.Dataset attendDataset = new ChartJsDto.Dataset("응시자수");
                        ChartJsDto.Dataset absentDataset = new ChartJsDto.Dataset("결시자수");
                        res.body().getContent().forEach(statusDto -> {
                            chartJsDto.addLabel(statusDto.getTypeNm());
                            attendDataset.addData(statusDto.getAttendCnt());
                            absentDataset.addData(statusDto.getAbsentCnt());
                        });
                        chartJsDto.addDataset(attendDataset);
                        chartJsDto.addDataset(absentDataset);
                        deferred.setResult(chartJsDto);
                    } else deferred.setErrorResult(res.errorBody());
                }, t -> deferred.setErrorResult(t.getMessage()));

        return deferred;
    }

    @RequestMapping(value = "dept", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<ChartJsDto> dept(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "1000") int rows
    ) throws IOException {

        DeferredResult<ChartJsDto> deferred = new DeferredResult<>();

        apiService.statusDept(QueryBuilder.getQueryString(q), page - 1, rows, JqgridMapper.getSortString(sidx, sord))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(res -> {
                    if (res.isSuccessful()) {
                        ChartJsDto chartJsDto = new ChartJsDto();
                        ChartJsDto.Dataset attendCnt = new ChartJsDto.Dataset("응시자수");
                        ChartJsDto.Dataset attendPer = new ChartJsDto.Dataset("응시율");
                        ChartJsDto.Dataset absentCnt = new ChartJsDto.Dataset("결시자수");
                        ChartJsDto.Dataset absentPer = new ChartJsDto.Dataset("결시율");
                        res.body().getContent().forEach(statusDto -> {
                            chartJsDto.addLabel(statusDto.getDeptNm());
                            attendCnt.addData(statusDto.getAttendCnt() == null ? 0 : statusDto.getAttendCnt());
                            attendPer.addData(statusDto.getAttendPer() == null ? 0 : statusDto.getAttendPer());
                            absentCnt.addData(statusDto.getAbsentCnt() == null ? 0 : statusDto.getAbsentCnt());
                            absentPer.addData(statusDto.getAbsentPer() == null ? 0 : statusDto.getAbsentPer());
                        });
                        chartJsDto.addDataset(attendCnt);
                        chartJsDto.addDataset(attendPer);
                        chartJsDto.addDataset(absentCnt);
                        chartJsDto.addDataset(absentPer);
                        deferred.setResult(chartJsDto);
                    } else deferred.setErrorResult(res.errorBody());
                }, t -> deferred.setErrorResult(t.getMessage()));

        return deferred;
    }
}
