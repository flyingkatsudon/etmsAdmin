package com.humane.etms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {

    @Value("${path.image.examinee:C:/api/image/examinee}") String pathExaminee;
    @Value("${path.image.noIdCard:C:/api/image/noIdCard}") String pathNoIdCard;
    @Value("${path.image.recheck:C:/api/image/recheck}") String pathRecheck;
    @Value("${path.image.signature:C:/api/image/signature}") String pathSignature;

    public InputStream getExaminee(String fileName) {
        return getFile(pathExaminee, fileName);
    }

    public InputStream getNoIdCard(String fileName) {
        return getFile(pathNoIdCard, fileName);
    }

    public InputStream getRecheck(String fileName) {
        return getFile(pathRecheck, fileName);
    }

    public InputStream getSignature(String fileName) {
        return getFile(pathSignature, fileName);
    }

    private InputStream getFile(String filePath, String fileName) {
        try {
            File path = new File(filePath);
            if (!path.exists()) path.mkdirs();
            File file = new File(path, fileName);
            if (file.exists()) {
                return new FileInputStream(file);
            }
        } catch (IOException ignored) {

        }
        return null;
    }

    public void deleteImage(String... path) {
        for (String p : path) {
            File examineePath = new File(p);
            File[] a = examineePath.listFiles();
            for (File file : a) {
                file.delete();
            }
        }
    }
}
