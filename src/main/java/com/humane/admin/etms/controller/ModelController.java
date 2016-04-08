package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.admin.etms.service.ResponseService;
import com.humane.util.query.QueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import rx.Observable;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("model")
@Slf4j
public class ModelController {
    @Autowired private ApiService apiService;
    @Autowired private ResponseService responseService;

    /**
     * 툴바 데이터를 전송
     */
    @RequestMapping(value = "toolbar")
    public ResponseEntity getToolbar() throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String query = new QueryBuilder()
//                .add("admissionCd", "1")
                .build();

        Observable<Response<List<StatusDto>>> observable = apiService.statusToolbar(query);
        return responseService.toResponseEntity(observable);
    }
}
