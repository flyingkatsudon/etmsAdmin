package com.humane.util.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
    public static File saveFile(String path, MultipartFile multipartFile, boolean isTime) throws IOException {
        return saveFile(new File(path), multipartFile, isTime);
    }

    public static File saveFile(String path, MultipartFile multipartFile) throws IOException {
        return saveFile(new File(path), multipartFile, true);
    }

    public static File saveFile(File path, MultipartFile multipartFile) throws IOException {
        return saveFile(path, multipartFile, true);
    }

    public static File saveFile(File path, MultipartFile multipartFile, boolean isTime) throws IOException {
        if (!path.exists()) path.mkdirs();

        String fileName = null;
        if (isTime) {
            String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            fileName = currentTime + "_" + multipartFile.getOriginalFilename();
        } else {
            fileName = multipartFile.getOriginalFilename();
        }

        File file = new File(path, fileName);
        multipartFile.transferTo(file);
        return file;
    }
}
