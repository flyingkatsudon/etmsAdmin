package com.humane.etms.controller.admin;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.DataMapper;
import com.humane.etms.service.ImageService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "data")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataController {
    private final DataMapper mapper;
    private final ImageService imageService;
    private static final String JSON = "json";
    private static final String PDF = "pdf";

    @Value("${path.image.examinee:C:/api/image/examinee}") String pathExaminee;
    @Value("${path.image.univLogo:C:/api/image/univLogo}") String pathUnivLogo;

    @RequestMapping(value = "examinee.{format:json|pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, StatusDto statusDto, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.examinee(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-examinee.jrxml",
                        format,
                        mapper.examinee(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "examineeId.pdf")
    public ResponseEntity examineeId(ExamineeDto examineeDto, Pageable pageable) {
        List<ExamineeDto> list = mapper.examinee(examineeDto, pageable).getContent();

        list.forEach(item -> {
            try (InputStream is = imageService.getFile(pathExaminee, item.getExamineeCd() + ".jpg")) {
                BufferedImage image = ImageIO.read(is);
                item.setExamineeImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (InputStream is = imageService.getFile(pathUnivLogo, "univLogo.png")) {
                BufferedImage image = ImageIO.read(is);
                item.setUnivLogo(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/examinee-id-card.jrxml",
                JasperReportsExportHelper.EXT_PDF,
                list
        );
    }

    @RequestMapping(value = "noIdCard.{format:json|pdf|xls|xlsx}")
    public ResponseEntity noIdCard(@PathVariable String format, ExamineeDto examineeDto, Pageable pageable) {
        examineeDto.setIsNoIdCard(true);
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.examinee(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-noIdCard.jrxml",
                        format,
                        mapper.examinee(examineeDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "recheck.{format:json|pdf|xls|xlsx}")
    public ResponseEntity recheck(@PathVariable String format, ExamineeDto examineeDto, Pageable pageable) {
        examineeDto.setIsCheck(true);
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.examinee(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-recheck.jrxml",
                        format,
                        mapper.examinee(examineeDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "otherHall.{format:json|pdf|xls|xlsx}")
    public ResponseEntity otherHall(@PathVariable String format, ExamineeDto examineeDto, Pageable pageable) {
        examineeDto.setIsOtherHall(true);

        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.examinee(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-otherHall.jrxml",
                        format,
                        mapper.examinee(examineeDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    /**
     * 감독관 서명 정보를 불러오기 위한 부분
     *
     * @param format    요청할 형태
     * @param statusDto 상태 정보
     * @param pageable
     * @return
     */
    @RequestMapping(value = "signature.{format:json|xlsx}")
    public ResponseEntity signature(@PathVariable String format, StatusDto statusDto, Pageable pageable) {

        switch (format) {
            case JSON:
                // 감독관 서명 정보를 요청함
                return ResponseEntity.ok(mapper.signature(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-signature.jrxml",
                        format,
                        mapper.signature(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "paper.{format:json|xlsx}")
    public ResponseEntity paper(@PathVariable String format, ExamineeDto statusDto, Pageable pageable) {

        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.paper(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-paper.jrxml",
                        format,
                        mapper.paper(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "detail.{format:json|pdf}")
    public ResponseEntity detail(@PathVariable String format, StatusDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.detail(param, pageable));
            case PDF:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-detail.jrxml"
                        , format
                        , mapper.detail(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
            default:
                return null;
        }
    }

    @RequestMapping(value = "checkIdCard")
    public String checkIdCard(String examineeCd) {
        Date idCheckDttm = new Date();
        mapper.checkIdCard(examineeCd, idCheckDttm);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(idCheckDttm);
    }

    @RequestMapping(value = "reCheck")
    public String reCheck(String examineeCd) {
        Date recheckDttm = new Date();
        mapper.recheck(examineeCd, recheckDttm);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recheckDttm);
    }

    @RequestMapping(value = "uplus.{format:xls|xlsx}")
    public ResponseEntity uplus(@PathVariable String format, ExamineeDto param, Pageable pageable) throws DRException {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/data-uplus.jrxml"
                , format
                , mapper.examinee(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
        );
    }
}