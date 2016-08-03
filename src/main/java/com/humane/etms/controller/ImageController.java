package com.humane.etms.controller;

import com.humane.etms.model.AttendHall;
import com.humane.etms.model.QAttendHall;
import com.humane.etms.repository.AttendHallRepository;
import com.humane.etms.service.ImageService;
import com.humane.util.file.FileUtils;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@RestController
@RequestMapping(value = "image", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageController {

    @Value("${path.image.recheck:C:/api/image/recheck}") String pathRecheck;
    @Value("${path.image.noIdCard:C:/api/image/noIdCard}") String pathNoIdCard;
    @Value("${path.image.noIdCard:C:/api/image/signature}") String pathSignature;

    private final ImageService imageService;
    private final AttendHallRepository attendHallRepository;

    @RequestMapping(value = "examinee/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> examinee(@PathVariable("fileName") String fileName) {
        InputStream inputStream = imageService.getExaminee(fileName);
        return ResponseEntity.ok(new InputStreamResource(inputStream));
    }

    @RequestMapping(value = "noIdCard", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> noIdCard(@RequestParam("file") MultipartFile file) {
        try {
            FileUtils.saveFile(pathNoIdCard, file, false);
            return ResponseEntity.ok(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "noIdCard/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> noIdCard(@PathVariable("fileName") String fileName) {
        InputStream inputStream = imageService.getNoIdCard(fileName);
        return ResponseEntity.ok(new InputStreamResource(inputStream));
    }

    @RequestMapping(value = "recheck", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> recheck(@RequestParam("file") MultipartFile file) {
        try {
            FileUtils.saveFile(pathRecheck, file, false);
            return ResponseEntity.ok(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "recheck/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> recheck(@PathVariable("fileName") String fileName) {
        InputStream inputStream = imageService.getRecheck(fileName);
        return ResponseEntity.ok(new InputStreamResource(inputStream));
    }

    @RequestMapping(value = "signature", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> signature(@RequestHeader("attendCd") String attendCd, @RequestHeader("hallCd") String hallCd, @RequestParam("file") MultipartFile file) {
        try {
            FileUtils.saveFile(pathSignature, file, false);

            QAttendHall qAttendHall = QAttendHall.attendHall;

            AttendHall attendHall = attendHallRepository.findOne(
                    new BooleanBuilder()
                            .and(qAttendHall.attend.attendCd.eq(attendCd))
                            .and(qAttendHall.hall.hallCd.eq(hallCd)));

            if(attendHall != null){
                attendHall.setSignDttm(new Date());
                attendHallRepository.save(attendHall);
            }

            return ResponseEntity.ok(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "signature/{fileName:.+}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> signature(@PathVariable("fileName") String fileName) {
        InputStream inputStream = imageService.getSignature(fileName);
        return ResponseEntity.ok(new InputStreamResource(inputStream));
    }
}
