package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import com.humane.util.jqgrid.JqgridMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("status")
@Slf4j
public class StatusController {

    private final ApiService apiService;

    @Autowired
    public StatusController(ApiService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping(value = "attend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JqgridMapper.JqgridResponse> attend(
            @RequestParam(value = "_search", required = false, defaultValue = "false") Boolean search,
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        Response<ResponseBody> response = apiService.statusAttend(
                search ? JqgridMapper.getQueryString(filters) : ""
                , page - 1
                , rows
                , Objects.equals(sidx, "") ? null : JqgridMapper.getSortString(sidx, sord)
        ).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "major", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JqgridMapper.JqgridResponse> major(
            @RequestParam(value = "_search", required = false, defaultValue = "false") Boolean search,
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        Response<ResponseBody> response = apiService.statusMajor(
                search ? JqgridMapper.getQueryString(filters) : ""
                , page - 1
                , rows
                , Objects.equals(sidx, "") ? null : JqgridMapper.getSortString(sidx, sord)
        ).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "dept", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JqgridMapper.JqgridResponse> dept(
            @RequestParam(value = "_search", required = false, defaultValue = "false") Boolean search,
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        Response<ResponseBody> response = apiService.statusDept(
                search ? JqgridMapper.getQueryString(filters) : ""
                , page - 1
                , rows
                , Objects.equals(sidx, "") ? null : JqgridMapper.getSortString(sidx, sord)
        ).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "hall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JqgridMapper.JqgridResponse> hall(
            @RequestParam(value = "_search", required = false, defaultValue = "false") Boolean search,
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        Response<ResponseBody> response = apiService.statusHall(
                search ? JqgridMapper.getQueryString(filters) : ""
                , page - 1
                , rows
                , Objects.equals(sidx, "") ? null : JqgridMapper.getSortString(sidx, sord)
        ).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "group", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JqgridMapper.JqgridResponse> group(
            @RequestParam(value = "_search", required = false, defaultValue = "false") Boolean search,
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        Response<ResponseBody> response = apiService.statusGroup(
                search ? JqgridMapper.getQueryString(filters) : ""
                , page - 1
                , rows
                , Objects.equals(sidx, "") ? null : JqgridMapper.getSortString(sidx, sord)
        ).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "examinee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JqgridMapper.JqgridResponse> examinee(
            @RequestParam(value = "_search", required = false, defaultValue = "false") Boolean search,
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        Response<ResponseBody> response = apiService.statusExaminee(
                search ? JqgridMapper.getQueryString(filters) : ""
                , page - 1
                , rows
                , Objects.equals(sidx, "") ? null : JqgridMapper.getSortString(sidx, sord)
        ).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }
}
