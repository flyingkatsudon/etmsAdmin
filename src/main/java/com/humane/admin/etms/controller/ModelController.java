package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.admin.etms.service.ResponseService;
import com.humane.util.ObjectConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import rx.Observable;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity getToolbar(StatusDto statusDto) {

        Map<String, String> params = ObjectConvert.<String, String>asMap(statusDto);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Observable<Response<List<StatusDto>>> observable = apiService.statusToolbar(params);
        return responseService.toResponseEntity(observable);
    }
}
