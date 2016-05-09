package com.humane.admin.etms.controller;

import com.humane.admin.etms.dto.ExamineeDto;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.admin.etms.service.ApiService;
import com.humane.admin.etms.service.ImageService;
import com.humane.util.ObjectConvert;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.jqgrid.JqgridPager;
import com.humane.util.spring.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

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
    private final ApiService apiService;
    private final ImageService imageService;
    private static final String LIST = "list";

    @RequestMapping(value = "examinee/{format:list|pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<ExamineeDto>> pageResponse = apiService.examinee(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort());
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                List<ExamineeDto> list = apiService.examinee(ObjectConvert.asMap(statusDto), pager.getSort());
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-examinee.jrxml",
                        format,
                        list
                );
        }
    }

    @RequestMapping(value = "examineeId/{format:pdf}")
    public ResponseEntity examineeId(@PathVariable String format, ExamineeDto examineeDto, JqgridPager pager, HttpServletResponse response) {
        List<ExamineeDto> list = apiService.examinee(ObjectConvert.asMap(examineeDto), pager.getSort());

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
    public ResponseEntity otherHall(@PathVariable String format, ExamineeDto examineeDto, JqgridPager pager, HttpServletResponse response) {
        examineeDto.setIsOtherHall(true);

        switch (format) {
            case LIST:
                Response<PageResponse<ExamineeDto>> pageResponse = apiService.examinee(
                        ObjectConvert.asMap(examineeDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort());
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                List<ExamineeDto> list = apiService.examinee(ObjectConvert.asMap(examineeDto), pager.getSort());
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-otherHall.jrxml",
                        format,
                        list
                );
        }
    }

    @RequestMapping(value = "noIdCard/{format:list|pdf|xls|xlsx}")
    public ResponseEntity noIdCard(@PathVariable String format, ExamineeDto examineeDto, JqgridPager pager, HttpServletResponse response) {
        examineeDto.setIsNoIdCard(true);
        switch (format) {
            case LIST:
                Response<PageResponse<ExamineeDto>> pageResponse = apiService.examinee(
                        ObjectConvert.asMap(examineeDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort());
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                List<ExamineeDto> list = apiService.examinee(ObjectConvert.asMap(examineeDto), pager.getSort());
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-noIdCard.jrxml",
                        format,
                        list
                );
        }
    }

    @RequestMapping(value = "recheck/{format:list|pdf|xls|xlsx}")
    public ResponseEntity recheck(@PathVariable String format, ExamineeDto examineeDto, JqgridPager pager, HttpServletResponse response) {
        examineeDto.setIsCheck(true);
        switch (format) {
            case LIST:
                Response<PageResponse<ExamineeDto>> pageResponse = apiService.examinee(
                        ObjectConvert.asMap(examineeDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort());
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                List<ExamineeDto> list = apiService.examinee(ObjectConvert.asMap(examineeDto), pager.getSort());
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-recheck.jrxml",
                        format,
                        list
                );
        }
    }

    /**
     *  감독관 서명 정보를 불러오기 위한 부분
     *
     * @param format 요청할 형태
     * @param statusDto 상태 정보
     * @param pager
     * @param response 정보가 저장됨
     * @return
     */
    @RequestMapping(value = "signature/{format:list|xlsx}")
    public ResponseEntity signature(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {

        switch (format) {
            case LIST:
                // 감독관 서명 정보를 요청함
                Response<PageResponse<StatusDto>> pageResponse = apiService.signature(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                List<StatusDto> list = apiService.signature(ObjectConvert.asMap(statusDto), pager.getSort());
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-signature.jrxml",
                        format,
                        list
                );
        }
    }

    @RequestMapping(value = "paper/{format:list|xlsx}")
    public ResponseEntity paper(@PathVariable String format, ExamineeDto statusDto, JqgridPager pager, HttpServletResponse response) {

        switch (format) {
            case LIST:
                Response<PageResponse<ExamineeDto>> pageResponse = apiService.paper(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                List<ExamineeDto> list = apiService.paper(ObjectConvert.asMap(statusDto), pager.getSort());
                return JasperReportsExportHelper.toResponseEntity(
                        response,
                        "jrxml/data-paper.jrxml",
                        format,
                        list
                );
        }
    }
}