package com.humane.etms.controller.admin;

import com.humane.etms.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "reset")
    public void reset(@RequestParam(defaultValue = "false") boolean photo) {
        systemService.resetData(photo);
    }

    @RequestMapping(value = "init")
    public void init(@RequestParam(defaultValue = "true") boolean photo) {
        systemService.initData(photo);
    }
}