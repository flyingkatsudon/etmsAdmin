package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.jqgrid.JqgridResponse;
import com.humane.util.query.QueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("status")
@Slf4j
public class StatusController {

    private final ApiService apiService;

    @Autowired
    public StatusController(ApiService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping(value = "attend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<JqgridResponse> attend(
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        DeferredResult<JqgridResponse> deferred = new DeferredResult<>();

        apiService.statusAttend(query, page - 1, rows, sort)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(res -> {
                    if (res.isSuccessful()) deferred.setResult(JqgridMapper.getResponse(res.body()));
                    else deferred.setErrorResult(res.errorBody());
                }, t -> deferred.setErrorResult(t.getMessage()));

        return deferred;
    }

    @RequestMapping(value = "major", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<JqgridResponse> major(
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        DeferredResult<JqgridResponse> deferred = new DeferredResult<>();

        apiService.statusMajor(query, page - 1, rows, sort)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(res -> {
                    if (res.isSuccessful()) deferred.setResult(JqgridMapper.getResponse(res.body()));
                    else deferred.setErrorResult(res.errorBody());
                }, t -> deferred.setErrorResult(t.getMessage()));

        return deferred;
    }

    @RequestMapping(value = "dept", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<JqgridResponse> dept(
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        DeferredResult<JqgridResponse> deferred = new DeferredResult<>();

        apiService.statusDept(query, page - 1, rows, sort)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(res -> {
                    if (res.isSuccessful()) deferred.setResult(JqgridMapper.getResponse(res.body()));
                    else deferred.setErrorResult(res.errorBody());
                }, t -> deferred.setErrorResult(t.getMessage()));

        return deferred;
    }

    @RequestMapping(value = "hall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<JqgridResponse> hall(
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        DeferredResult<JqgridResponse> deferred = new DeferredResult<>();

        apiService.statusHall(query, page - 1, rows, sort)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(res -> {
                    if (res.isSuccessful()) deferred.setResult(JqgridMapper.getResponse(res.body()));
                    else deferred.setErrorResult(res.errorBody());
                }, t -> deferred.setErrorResult(t.getMessage()));

        return deferred;
    }

    @RequestMapping(value = "group", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<JqgridResponse> group(
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        DeferredResult<JqgridResponse> deferred = new DeferredResult<>();

        apiService.statusGroup(query, page - 1, rows, sort)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(res -> {
                    if (res.isSuccessful()) deferred.setResult(JqgridMapper.getResponse(res.body()));
                    else deferred.setErrorResult(res.errorBody());
                }, t -> deferred.setErrorResult(t.getMessage()));

        return deferred;
    }

    @RequestMapping(value = "examinee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeferredResult<JqgridResponse> examinee(
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        DeferredResult<JqgridResponse> deferred = new DeferredResult<>();

        apiService.statusExaminee(query, page - 1, rows, sort)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(res -> {
                    if (res.isSuccessful()) deferred.setResult(JqgridMapper.getResponse(res.body()));
                    else deferred.setErrorResult(res.errorBody());
                }, t -> deferred.setErrorResult(t.getMessage()));

        return deferred;
    }


    /**
     * 툴바 데이터를 전송
     */
    @RequestMapping(value = "toolbar")
    public DeferredResult<List<StatusDto>> getToolbar() throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        DeferredResult<List<StatusDto>> deferred = new DeferredResult<>();
        String query = new QueryBuilder()
//                .add("admissionCd", "1")
                .build();
        
        apiService.statusToolbar(query)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(res -> {
                    if (res.isSuccessful()) deferred.setResult(res.body());
                    else deferred.setErrorResult(res.errorBody());
                }, t -> deferred.setErrorResult(t.getMessage()));

        return deferred;
    }
}
