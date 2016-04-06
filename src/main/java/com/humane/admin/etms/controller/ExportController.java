package com.humane.admin.etms.controller;

import com.humane.admin.etms.service.ExportService;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.query.QueryBuilder;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.report.exception.DRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.schedulers.Schedulers;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
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
                       HttpServletRequest request
    ) throws IOException, DRException {
        final AsyncContext async = request.startAsync();

        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        async.start(() -> {
            HttpServletResponse asyncRes = (HttpServletResponse) async.getResponse();

            exportService.reportAttend(query, sort)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.newThread())
                    .subscribe(
                            jasperReportBuilder -> exportService.toXlsx(asyncRes, jasperReportBuilder, "전형별 응시율")
                            , Throwable::printStackTrace
                            , async::complete
                    );
        });
    }

    @RequestMapping("dept")
    public void dept(@RequestParam(value = "q", required = false) String q,
                     @RequestParam(value = "sidx", required = false) String sidx,
                     @RequestParam(value = "sord", required = false) String sord,
                     HttpServletRequest request
    ) throws IOException, DRException {
        final AsyncContext async = request.startAsync();
        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);
        async.start(() -> {
            HttpServletResponse asyncRes = (HttpServletResponse) async.getResponse();

            exportService.reportDept(query, sort)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.newThread())
                    .subscribe(
                            jasperReportBuilder -> exportService.toXlsx(asyncRes, jasperReportBuilder, "모집단위별 응시율")
                            , Throwable::printStackTrace
                            , async::complete);
        });
    }

    @RequestMapping("hall")
    public void hall(@RequestParam(value = "q", required = false) String q,
                     @RequestParam(value = "sidx", required = false) String sidx,
                     @RequestParam(value = "sord", required = false) String sord,
                     HttpServletRequest request
    ) throws IOException, DRException {
        final AsyncContext async = request.startAsync();
        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);
        async.start(() -> {
            HttpServletResponse asyncRes = (HttpServletResponse) async.getResponse();

            exportService.reportHall(query, sort)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.newThread())
                    .subscribe(
                            jasperReportBuilder -> exportService.toXlsx(asyncRes, jasperReportBuilder, "고사실별 응시율")
                            , Throwable::printStackTrace
                            , async::complete);
        });
    }

    @RequestMapping("examinee")
    public void examinee(@RequestParam(value = "q", required = false) String q,
                         @RequestParam(value = "sidx", required = false) String sidx,
                         @RequestParam(value = "sord", required = false) String sord,
                         HttpServletRequest request
    ) throws IOException, DRException {
        String query = QueryBuilder.getQueryString(q);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        final AsyncContext async = request.startAsync();
        async.start(() -> {
            HttpServletResponse asyncRes = (HttpServletResponse) async.getResponse();

            exportService.reportExaminee(query, sort)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.newThread())
                    .subscribe(
                            jasperReportBuilder -> exportService.toXlsx(asyncRes, jasperReportBuilder, "수험생별 응시현황")
                            , Throwable::printStackTrace
                            , async::complete
                    );
        });
    }
}
