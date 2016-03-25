package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.query.QueryBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.Response;
import rx.Observable;
import rx.schedulers.Schedulers;

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
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusAttend(query, page - 1, rows, sort).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "major", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JqgridMapper.JqgridResponse> major(
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusMajor(query, page - 1, rows, sort).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "dept", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JqgridMapper.JqgridResponse> dept(
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusDept(query, page - 1, rows, sort).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "hall", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JqgridMapper.JqgridResponse> hall(
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusHall(query, page - 1, rows, sort).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "group", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JqgridMapper.JqgridResponse> group(
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusGroup(query, page - 1, rows, sort).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "examinee", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JqgridMapper.JqgridResponse> examinee(
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "rows", required = false, defaultValue = "100") Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusExaminee(query, page - 1, rows, sort).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }


    /**
     * 툴바 데이터를 전송
     */
    @RequestMapping(value = "toolbar")
    public ResponseEntity<InputStreamResource> getToolbar() throws IOException {

        // 1. 파라미터 생성(데이터를 가져올때 로그인한 자기것만 가져온다.)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // TODO : 수정할 것
        String query = new QueryBuilder()
//                .add("admissionCd", "1")
                .build();

        // 2. api 서버에서 데이터를 가져온다.
        Response<ResponseBody> response = apiService.statusToolbar(query).execute();

        // 3. 가져온 데이터가 정상적이면 데이터를 전송한다.
        if (response.isSuccessful()) return ResponseEntity.ok(new InputStreamResource(response.body().byteStream()));

        // 4. 가져온 데이터가 에러면 에러 표시
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }
}
