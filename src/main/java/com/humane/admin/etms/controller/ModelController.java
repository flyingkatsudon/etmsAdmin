package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.RestApi;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.admin.etms.service.ResponseService;
import com.humane.util.ObjectConvert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import rx.Observable;

import java.util.List;

@RestController
@RequestMapping(value = "model", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ModelController {
    private final RestApi restApi;
    private final ResponseService responseService;

    /**
     * 툴바 데이터를 전송
     */
    @RequestMapping(value = "toolbar")
    public ResponseEntity getToolbar(StatusDto statusDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Observable<Response<List<StatusDto>>> observable = restApi.statusToolbar(ObjectConvert.asMap(statusDto));
        return responseService.toResponseEntity(observable);
    }
}
