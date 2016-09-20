package com.humane.etms.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Service
@Slf4j
public class ImageService {
    public ResponseEntity<InputStreamResource> toResponseEntity(String path, String fileName) {
        try {
            InputStream inputStream = getFile(path, fileName);
            return ResponseEntity.ok(new InputStreamResource(inputStream));
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    public InputStream getFile(String filePath, String fileName) throws FileNotFoundException {
        File path = new File(filePath);
        if (!path.exists()) path.mkdirs();
        File file = new File(path, fileName);
        return new FileInputStream(file);
    }

    public void deleteImage(String... paths) {
        for (String path : paths) {
            File filePath = new File(path);
            if (filePath.exists()) {
                File[] listFiles = filePath.listFiles();
                if (listFiles != null) {
                    for (File file : listFiles) {
                        file.delete();
                    }
                }
            }
        }
    }
}
