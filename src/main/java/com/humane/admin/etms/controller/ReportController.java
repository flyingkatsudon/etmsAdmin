package com.humane.admin.etms.controller;

import com.humane.admin.etms.dto.StatusDto;
import com.humane.admin.etms.service.ReportService;
import com.humane.admin.etms.service.ImageService;
import com.humane.admin.etms.service.ExportService;
import com.humane.util.ObjectConvert;
import com.humane.util.jqgrid.JqgridPager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("report")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportController {
    private final ReportService reportService;
    private final ImageService imageService;
    private final ExportService exportService;

    @RequestMapping("attend")
    public void attend(StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        Observable<JasperReportBuilder> observable = reportService.reportAttend(
                ObjectConvert.asMap(statusDto),
                pager.getSort()
        );

        JasperReportBuilder builder = observable.toBlocking().first();

        exportService.toXlsx(response, builder, "전형 별 응시율");
    }

    @RequestMapping("dept")
    public void dept(StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        Observable<JasperReportBuilder> observable = reportService.reportDept(
                ObjectConvert.asMap(statusDto),
                pager.getSort()
        );

        JasperReportBuilder builder = observable.toBlocking().first();

        exportService.toXlsx(response, builder, "모집단위 별 응시율");
    }

    @RequestMapping("hall")
    public void hall(StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        Observable<JasperReportBuilder> observable = reportService.reportHall(
                ObjectConvert.asMap(statusDto),
                pager.getSort()
        );

        JasperReportBuilder builder = observable.toBlocking().first();

        exportService.toXlsx(response, builder, "고사실 별 응시율");
    }

    @RequestMapping("group")
    void group(StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        Observable<JasperReportBuilder> observable = reportService.reportGroup(
                ObjectConvert.asMap(statusDto),
                pager.getSort()
        );

        JasperReportBuilder builder = observable.toBlocking().first();

        exportService.toXlsx(response, builder, "조 별 응시율");
    }

    @RequestMapping("examinee")
    public void examinee(StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        Observable<JasperReportBuilder> observable = reportService.reportExaminee(
                ObjectConvert.asMap(statusDto),
                pager.getSort()
        );

        JasperReportBuilder builder = observable.toBlocking().first();

        exportService.toXlsx(response, builder, "수험생 별 응시현황");
    }

    @RequestMapping("examineeId")
    public void examineeId(StatusDto statusDto, HttpServletResponse response) {
        Observable<ArrayList<StatusDto>> observable = reportService.getAllExaminee(ObjectConvert.asMap(statusDto));

        List<StatusDto> list = observable.toBlocking().first();

        list.forEach(item -> {
            try (InputStream is = imageService.getImageExaminee(item.getExamineeCd() + ".jpg")) {
                BufferedImage image = ImageIO.read(is);
                item.setExamineeImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        JasperPrint jasperPrint = reportService.getPrint("jrxml/examinee-id-card.jrxml", new HashMap<>(), list);
        exportService.toPdf(response, jasperPrint, "수험표");
    }
}