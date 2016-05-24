package com.humane.etms.controller.admin;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.DataMapper;
import com.humane.etms.service.ImageService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value = "data")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataController {
    private final DataMapper mapper;
    private final ImageService imageService;
    private static final String LIST = "list";

    @RequestMapping(value = "examinee/{format:list|pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, StatusDto statusDto, Pageable page, HttpServletResponse response) {
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.examinee(statusDto, page));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-examinee.jrxml",
                        format,
                        mapper.examinee(statusDto, page).getContent()
                );
        }
    }

    @RequestMapping(value = "examineeId/{format:pdf}")
    public ResponseEntity examineeId(@PathVariable String format, ExamineeDto examineeDto, PageRequest page, HttpServletResponse response) {
        List<StatusDto> list = mapper.examinee(examineeDto, page).getContent();

        list.forEach(item -> {
            try (InputStream is = imageService.getImageExaminee(item.getExamineeCd() + ".jpg")) {
                BufferedImage image = ImageIO.read(is);
                item.setExamineeImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return JasperReportsExportHelper.toResponseEntity(
                response,
                "jrxml/examinee-id-card.jrxml",
                format,
                list
        );
    }

    @RequestMapping(value = "otherHall/{format:list|pdf|xls|xlsx}")
    public ResponseEntity otherHall(@PathVariable String format, ExamineeDto examineeDto, Pageable pageable, HttpServletResponse response) {
        examineeDto.setIsOtherHall(true);

        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.examinee(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-otherHall.jrxml",
                        format,
                        mapper.examinee(examineeDto, pageable).getContent()
                );
        }
    }

    @RequestMapping(value = "noIdCard/{format:list|pdf|xls|xlsx}")
    public ResponseEntity noIdCard(@PathVariable String format, ExamineeDto examineeDto, Pageable pageable, HttpServletResponse response) {
        examineeDto.setIsNoIdCard(true);
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.examinee(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-noIdCard.jrxml",
                        format,
                        mapper.examinee(examineeDto, pageable).getContent()
                );
        }
    }

    @RequestMapping(value = "recheck/{format:list|pdf|xls|xlsx}")
    public ResponseEntity recheck(@PathVariable String format, ExamineeDto examineeDto, Pageable pageable, HttpServletResponse response) {
        examineeDto.setIsCheck(true);
        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.examinee(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-recheck.jrxml",
                        format,
                        mapper.examinee(examineeDto, pageable).getContent()
                );
        }
    }

    /**
     * 감독관 서명 정보를 불러오기 위한 부분
     *
     * @param format    요청할 형태
     * @param statusDto 상태 정보
     * @param pageable
     * @param response  정보가 저장됨
     * @return
     */
    @RequestMapping(value = "signature/{format:list|xlsx}")
    public ResponseEntity signature(@PathVariable String format, StatusDto statusDto, Pageable pageable, HttpServletResponse response) {

        switch (format) {
            case LIST:
                // 감독관 서명 정보를 요청함
                return ResponseEntity.ok(mapper.signature(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-signature.jrxml",
                        format,
                        mapper.signature(statusDto, pageable).getContent()
                );
        }
    }

    @RequestMapping(value = "paper/{format:list|xlsx}")
    public ResponseEntity paper(@PathVariable String format, ExamineeDto statusDto, Pageable pageable, HttpServletResponse response) {

        switch (format) {
            case LIST:
                return ResponseEntity.ok(mapper.paper(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-paper.jrxml",
                        format,
                        mapper.paper(statusDto, pageable).getContent()
                );
        }
    }
}