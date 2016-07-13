package com.humane.etms.controller.admin;

import com.humane.etms.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "system", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemController {
    private final SystemService systemService;

    @RequestMapping(value = "download", method = RequestMethod.POST)
    public ResponseEntity download() {
        return ResponseEntity.ok("준비 중입니다.");
    }

    private static final String PHOTO = "photo";

    @RequestMapping(value = "reset.{format:photo|none}")
    public void reset(@PathVariable String format) {
        switch(format){
            case PHOTO:
                systemService.resetData(format);
            default:
                systemService.resetData(null);
        }
    }
}