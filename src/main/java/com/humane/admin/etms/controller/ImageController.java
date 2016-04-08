package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import com.humane.admin.etms.service.ResponseService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import rx.Observable;

import java.io.IOException;

@RestController
@RequestMapping(value = "image", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@Slf4j
public class ImageController {

    @Autowired private ApiService apiService;
    @Autowired private ResponseService responseService;

    @RequestMapping(value = "examinee/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> examinee(@PathVariable("fileName") String fileName) throws IOException {

        Observable<Response<ResponseBody>> observable = apiService.imageExaminee(fileName);
        return responseService.toResourceEntity(observable);
    }
}
