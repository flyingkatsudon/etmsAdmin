package com.humane.etms.admin.controller;

import com.humane.etms.admin.api.ApiService;
import com.humane.util.JqgridMapper;
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
            @RequestParam("_search") Boolean search,
            @RequestParam(value = "filters", required = false) String filters,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord) throws IOException {

        String query = search ? JqgridMapper.getQueryString(filters) : "";

        Response<ResponseBody> response = apiService.hall(query, page - 1, rows).execute();
        if (response.isSuccessful()) return ResponseEntity.ok(JqgridMapper.getResponse(response.body().byteStream()));

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }
}
