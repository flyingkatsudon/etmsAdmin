package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
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

    @RequestMapping(value = "examinee/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> examinee(@PathVariable("fileName") String fileName) throws IOException {

        Observable<Response<ResponseBody>> observable = apiService.imageExaminee(fileName);
        try {
            Response<ResponseBody> response = observable.toBlocking().first();
            if (response.isSuccessful())
                return ResponseEntity.ok(new InputStreamResource(response.body().byteStream()));
            else {
                log.error("{}", response.errorBody());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }
}
