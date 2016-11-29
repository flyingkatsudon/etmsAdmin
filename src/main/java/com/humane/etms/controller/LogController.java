package com.humane.etms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "api/log")
@Slf4j
public class LogController {

    @Value("${path.logs:C:/api/logs}") String pathLogs;

    @RequestMapping(method = RequestMethod.POST)
    public void save(@RequestHeader("attendCd") String attendCd, @RequestHeader("hallCd") String hallCd, @RequestParam("file") MultipartFile file) throws IOException {
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = attendCd + "_" + hallCd + "_" + currentTime + "_logs.zip";
        File path = new File(pathLogs);
        if (!path.exists()) path.mkdirs();

        try {
            file.transferTo(new File(path, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
