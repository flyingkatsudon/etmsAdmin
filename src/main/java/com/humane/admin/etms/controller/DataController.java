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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import rx.Observable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping(value = "data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataController {
    private final ApiService apiService;
    private final ImageService imageService;

    @RequestMapping(value = "examinee/list")
    public ResponseEntity examinee(ExamineeDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<ExamineeDto>>> observable = apiService.examinee(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort());

        Response<PageResponse<ExamineeDto>> response = observable.toBlocking().first();

        return ResponseEntity.ok(JqgridMapper.getResponse(response.body()));
    }

    @RequestMapping(value = "examinee/{format:pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, StatusDto statusDto, JqgridPager pager) {
        Observable<List<ExamineeDto>> observable = apiService.examinee(
                ObjectConvert.asMap(statusDto),
                pager.getSort()
        );

        List<ExamineeDto> list = observable.toBlocking().first();

        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/data-examinee.jrxml",
                format,
                list
        );
    }

    @RequestMapping(value = "examineeId/{format:pdf}")
    public ResponseEntity examineeId(@PathVariable String format, ExamineeDto examineeDto, JqgridPager pager) {
        Observable<List<ExamineeDto>> observable = apiService.examinee(
                ObjectConvert.asMap(examineeDto),
                pager.getSort()
        );

        List<ExamineeDto> list = observable.toBlocking().first();

        list.forEach(item -> {
            try (InputStream is = imageService.getImageExaminee(item.getExamineeCd() + ".jpg")) {
                BufferedImage image = ImageIO.read(is);
                item.setExamineeImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/examinee-id-card.jrxml",
                format,
                list
        );
    }

    @RequestMapping(value = "otherHall/list")
    public ResponseEntity otherHall(ExamineeDto examineeDto, JqgridPager pager) {
        examineeDto.setIsOtherHall(true);

        Observable<Response<PageResponse<ExamineeDto>>> observable = apiService.examinee(
                ObjectConvert.asMap(examineeDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort());

        Response<PageResponse<ExamineeDto>> response = observable.toBlocking().first();

        return ResponseEntity.ok(JqgridMapper.getResponse(response.body()));
    }

    @RequestMapping(value = "otherHall/{format:pdf|xls|xlsx}")
    public ResponseEntity otherHall(@PathVariable String format, ExamineeDto examineeDto, JqgridPager pager) {

        examineeDto.setIsOtherHall(true);

        Observable<List<ExamineeDto>> observable = apiService.examinee(
                ObjectConvert.asMap(examineeDto),
                pager.getSort()
        );
        List<ExamineeDto> list = observable.toBlocking().first();

        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/data-otherHall.jrxml",
                format,
                list
        );
    }

    @RequestMapping(value = "noIdCard/list")
    public ResponseEntity noIdCard(ExamineeDto examineeDto, JqgridPager pager) {
        examineeDto.setIsNoIdCard(true);

        Observable<Response<PageResponse<ExamineeDto>>> observable = apiService.examinee(
                ObjectConvert.asMap(examineeDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort());

        Response<PageResponse<ExamineeDto>> response = observable.toBlocking().first();

        return ResponseEntity.ok(JqgridMapper.getResponse(response.body()));
    }

    @RequestMapping(value = "noIdCard/{format:pdf|xls|xlsx}")
    public ResponseEntity noIdCard(@PathVariable String format, ExamineeDto examineeDto, JqgridPager pager) {
        examineeDto.setIsNoIdCard(true);

        Observable<List<ExamineeDto>> observable = apiService.examinee(
                ObjectConvert.asMap(examineeDto),
                pager.getSort()
        );
        List<ExamineeDto> list = observable.toBlocking().first();

        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/data-noIdCard.jrxml",
                format,
                list
        );
    }

    @RequestMapping(value = "recheck/list")
    public ResponseEntity recheck(ExamineeDto examineeDto, JqgridPager pager) {
        examineeDto.setIsRecheck(true);

        Observable<Response<PageResponse<ExamineeDto>>> observable = apiService.examinee(
                ObjectConvert.asMap(examineeDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort());

        Response<PageResponse<ExamineeDto>> response = observable.toBlocking().first();

        return ResponseEntity.ok(JqgridMapper.getResponse(response.body()));
    }

    @RequestMapping(value = "recheck/{format:pdf|xls|xlsx}")
    public ResponseEntity recheck(@PathVariable String format, ExamineeDto examineeDto, JqgridPager pager) {
        examineeDto.setIsRecheck(true);

        Observable<List<ExamineeDto>> observable = apiService.examinee(
                ObjectConvert.asMap(examineeDto),
                pager.getSort()
        );
        List<ExamineeDto> list = observable.toBlocking().first();

        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/data-noIdCard.jrxml",
                format,
                list
        );
    }
}