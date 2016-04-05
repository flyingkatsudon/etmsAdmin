package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.schedulers.Schedulers;

import java.io.IOException;

@RestController
@RequestMapping(value = "image", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ImageController {

    @Autowired private ApiService apiService;

    @RequestMapping(value = "examinee/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public DeferredResult<InputStreamResource> examinee(@PathVariable("fileName") String fileName) throws IOException {

        DeferredResult<InputStreamResource> deferred = new DeferredResult<>();

        apiService.imageExaminee(fileName)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(response -> {
                    if (response.isSuccessful())
                        deferred.setResult(new InputStreamResource(response.body().byteStream()));
                    else deferred.setErrorResult(response.errorBody());
                }, deferred::setErrorResult);

        return deferred;
    }
}
