package com.humane.etms.admin.controller;

import com.humane.etms.admin.api.ApiService;
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

import java.io.IOException;

@RestController
@RequestMapping(value = "image", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ImageController {

    private final ApiService apiService;

    @Autowired
    public ImageController(ApiService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping(value = "examinee/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> examinee(@PathVariable("fileName") String fileName) throws IOException {
        Response<ResponseBody> a = apiService.imageExaminee(fileName).execute();
        if (a.isSuccessful()) {
            return ResponseEntity.ok(new InputStreamResource(a.body().byteStream()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
