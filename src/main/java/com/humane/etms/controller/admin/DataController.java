package com.humane.etms.controller.admin;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.DataMapper;
import com.humane.etms.service.DataService;
import com.humane.etms.service.ImageService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
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
    private final DataService dataService;
    private final ImageService imageService;
    private static final String JSON = "json";
    private static final String PDF = "pdf";
    private static final String COLMODEL = "colmodel";
/*

    @RequestMapping(value = "examinee.{format:json|pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, StatusDto statusDto, Pageable page) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.examinee(statusDto, page));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-examinee.jrxml",
                        format,
                        mapper.examinee(statusDto, page).getContent()
                );
        }
    }
*/

    @RequestMapping(value = "examinee.{format:colmodel|json|pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, ExamineeDto param, Pageable pageable) throws DRException {
        switch (format) {
            case COLMODEL:
                return ResponseEntity.ok(dataService.getExamineeModel());
            case JSON:
                return ResponseEntity.ok(mapper.examinee(param, pageable));
            default:
                JasperReportBuilder report = dataService.getExamineeReport();
                report.setDataSource(mapper.examinee(param, pageable).getContent());

                JasperPrint jasperPrint = report.toJasperPrint();
                jasperPrint.setName("수험생 별 종합");

                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }

    @RequestMapping(value = "examineeId.pdf")
    public ResponseEntity examineeId(ExamineeDto examineeDto, Pageable pageable) {
        List<ExamineeDto> list = mapper.examinee(examineeDto, pageable).getContent();

        list.forEach(item -> {
            try (InputStream is = imageService.getExaminee(item.getExamineeCd() + ".jpg")) {
                BufferedImage image = ImageIO.read(is);
                item.setExamineeImage(image);
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
                        mapper.examinee(examineeDto, pageable).getContent()
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
                        mapper.examinee(examineeDto, pageable).getContent()
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
                        mapper.signature(statusDto, pageable).getContent()
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
                        mapper.paper(statusDto, pageable).getContent()
                );
        }
    }

    @RequestMapping(value = "detail.{format:json|pdf}")
    public ResponseEntity detail(@PathVariable String format, StatusDto param, Pageable pageable){
        switch(format) {
            case JSON:
                return ResponseEntity.ok(mapper.detail(param, pageable));
            case PDF:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-detail.jrxml"
                        , format
                        , mapper.detail(param, pageable).getContent()
                );
            default:
                return null;
        }
    }
}