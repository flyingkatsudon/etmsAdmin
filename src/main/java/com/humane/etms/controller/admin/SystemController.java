package com.humane.etms.controller.admin;

import com.humane.etms.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public void init() {
        systemService.initData();
    }

    @RequestMapping(value = "initMgr")
    public void initMgr(@RequestParam String admissionCd, @RequestParam String attendDate, @RequestParam String headNm, @RequestParam String bldgNm) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(attendDate);
        systemService.initMgr(admissionCd, date, headNm, bldgNm);
    }

    @RequestMapping(value = "initApp")
    public void initApp(@RequestParam String attendCd, @RequestParam String attendHallCd) {
        systemService.initApp(attendCd, attendHallCd);
    }
}