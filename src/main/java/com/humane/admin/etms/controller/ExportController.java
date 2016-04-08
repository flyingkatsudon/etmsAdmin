package com.humane.admin.etms.controller;

import com.humane.admin.etms.service.ExportService;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.query.QueryBuilder;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("export")
@Slf4j
public class ExportController {

    @Autowired private ExportService exportService;

    @RequestMapping("attend")
    public void attend(@RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "sidx", required = false) String sidx,
                       @RequestParam(value = "sord", required = false) String sord,
                       HttpServletResponse response
    ) throws IOException, DRException {

        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<JasperReportBuilder> observable = exportService.reportAttend(query, sort);

        try {
            JasperReportBuilder builder = observable.toBlocking().first();
            exportService.toXlsx(response, builder, "전형별 응시율");
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    @RequestMapping("dept")
    public void dept(@RequestParam(value = "q", required = false) String q,
                     @RequestParam(value = "sidx", required = false) String sidx,
                     @RequestParam(value = "sord", required = false) String sord,
                     HttpServletResponse response
    ) throws IOException, DRException {
        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<JasperReportBuilder> observable = exportService.reportDept(query, sort);

        try {
            JasperReportBuilder builder = observable.toBlocking().first();
            exportService.toXlsx(response, builder, "모집단위별 응시율");
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    @RequestMapping("hall")
    public void hall(@RequestParam(value = "q", required = false) String q,
                     @RequestParam(value = "sidx", required = false) String sidx,
                     @RequestParam(value = "sord", required = false) String sord,
                     HttpServletResponse response
    ) throws IOException, DRException {
        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<JasperReportBuilder> observable = exportService.reportHall(query, sort);
        try {
            JasperReportBuilder builder = observable.toBlocking().first();
            exportService.toXlsx(response, builder, "고사실별 응시율");
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    @RequestMapping("group")
    void group(@RequestParam(value = "q", required = false) String q,
               @RequestParam(value = "sidx", required = false) String sidx,
               @RequestParam(value = "sord", required = false) String sord,
               HttpServletResponse response) throws IOException {

        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<JasperReportBuilder> observable = exportService.reportGroup(query, sort);
        try {
            JasperReportBuilder builder = observable.toBlocking().first();
            exportService.toXlsx(response, builder, "조별 응시율");
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }


    @RequestMapping("examinee")
    public void examinee(@RequestParam(value = "q", required = false) String q,
                         @RequestParam(value = "sidx", required = false) String sidx,
                         @RequestParam(value = "sord", required = false) String sord,
                         HttpServletResponse response
    ) throws IOException, DRException {
        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable<JasperReportBuilder> observable = exportService.reportExaminee(query, sort);

        try {
            JasperReportBuilder builder = observable.toBlocking().first();
            exportService.toXlsx(response, builder, "수험생별 응시현황");
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }
}
