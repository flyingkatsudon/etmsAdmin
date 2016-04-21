package com.humane.admin.etms.controller;

import com.humane.admin.etms.dto.StatusDto;
import com.humane.admin.etms.service.ExportService;
import com.humane.util.ObjectConvert;
import com.humane.util.jqgrid.JqgridMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("export")
@Slf4j
public class ExportController {

    @Autowired private ExportService exportService;

    @RequestMapping("attend")
    public void attend(
            StatusDto statusDto,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            HttpServletResponse response
    ) {

        Map<String, String> params = ObjectConvert.<String, String>asMap(statusDto);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<JasperReportBuilder> observable = exportService.reportAttend(params, sort);

        try {
            JasperReportBuilder builder = observable.toBlocking().first();
            exportService.toXlsx(response, builder, "전형 별 응시율");
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    @RequestMapping("dept")
    public void dept(
            StatusDto statusDto,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            HttpServletResponse response
    ) {
        Map<String, String> params = ObjectConvert.<String, String>asMap(statusDto);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<JasperReportBuilder> observable = exportService.reportDept(params, sort);

        try {
            JasperReportBuilder builder = observable.toBlocking().first();
            exportService.toXlsx(response, builder, "모집단위 별 응시율");
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    @RequestMapping("hall")
    public void hall(
            StatusDto statusDto,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            HttpServletResponse response
    ) {
        Map<String, String> params = ObjectConvert.<String, String>asMap(statusDto);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<JasperReportBuilder> observable = exportService.reportHall(params, sort);
        try {
            JasperReportBuilder builder = observable.toBlocking().first();
            exportService.toXlsx(response, builder, "고사실 별 응시율");
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    @RequestMapping("group")
    void group(
            StatusDto statusDto,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            HttpServletResponse response
    ) {

        Map<String, String> params = ObjectConvert.<String, String>asMap(statusDto);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<JasperReportBuilder> observable = exportService.reportGroup(params, sort);
        try {
            JasperReportBuilder builder = observable.toBlocking().first();
            exportService.toXlsx(response, builder, "조 별 응시율");
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    @RequestMapping("examinee")
    public void examinee(
            StatusDto statusDto,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            HttpServletResponse response
    ) {
        Map<String, String> params = ObjectConvert.<String, String>asMap(statusDto);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<JasperReportBuilder> observable = exportService.reportExaminee(params, sort);

        try {
            JasperReportBuilder builder = observable.toBlocking().first();
            exportService.toXlsx(response, builder, "수험생 별 응시현황");
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    @RequestMapping("examineeId")
    public void examineeId(
            StatusDto statusDto,
            HttpServletResponse response
    ) {
        Map<String, String> params = ObjectConvert.<String, String>asMap(statusDto);
        Observable<ArrayList<StatusDto>> observable = exportService.getAllExaminee(params);
        ArrayList<StatusDto> list = observable.toBlocking().first();

        JasperPrint jasperPrint = exportService.getPrint("jrxml/examinee-id-card.jrxml", new HashMap<>(), list);
        exportService.toPdf(response, jasperPrint, "수험표");
    }
}

