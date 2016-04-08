package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.admin.etms.service.ResponseService;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.query.QueryBuilder;
import com.humane.util.spring.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import rx.Observable;

import java.io.IOException;

@RestController
@RequestMapping("data")
@Slf4j
public class DataController {

    @Autowired private ApiService apiService;
    @Autowired private ResponseService responseService;

    @RequestMapping(value = "examinee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity examinee(
            StatusDto statusDto,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord
    ) throws IOException {

        String query = new QueryBuilder(statusDto).build();
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<Response<PageResponse<StatusDto>>> observable = apiService.statusExaminee(query, page - 1, rows, sort);
        return responseService.toJqgridResponseEntity(observable);
    }
}