package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import com.humane.util.file.FileNameEncoder;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.query.QueryBuilder;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.constant.JasperProperty;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.schedulers.Schedulers;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

@RestController
@RequestMapping("export")
@Slf4j
public class ExportController {
    private static final String TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Autowired private ApiService apiService;

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

            Observable.range(0, Integer.MAX_VALUE)
                    .concatMap(currentPage -> apiService.statusAttend(query, currentPage, Integer.MAX_VALUE, sort))
                    .takeUntil(pageResponse -> pageResponse.body().isLast())
                    .reduce(new ArrayList<>(), (list, pageResponse) -> {
                        list.addAll(pageResponse.body().getContent());
                        return list;
                    })
                    .map(list -> report()
                            .columns(col.column("전형", "admissionNm", type.stringType()),
                                    col.column("계열", "typeNm", type.stringType()),
                                    col.column("시험일자", "attendDate", type.dateType()),
                                    col.column("시험시간", "attendTime", type.timeHourToSecondType()),
                                    col.column("지원자수", "examineeCnt", type.longType()),
                                    col.column("응시자수", "attendCnt", type.longType()),
                                    col.column("응시율", "attendPer", type.longType()),
                                    col.column("결시자수", "absentCnt", type.longType()),
                                    col.column("결시율", "absentPer", type.longType())
                            ).setDataSource(new JRBeanCollectionDataSource(list)))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.newThread())
                    .subscribe(
                            jasperReportBuilder -> toXlsx(asyncRes, jasperReportBuilder, "전형별 응시율"),
                            Throwable::printStackTrace,
                            async::complete);
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

            Observable.range(0, Integer.MAX_VALUE)
                    .concatMap(currentPage -> apiService.statusDept(query, currentPage, Integer.MAX_VALUE, sort))
                    .takeUntil(pageResponse -> pageResponse.body().isLast())
                    .reduce(new ArrayList<>(), (list, pageResponse) -> {
                        list.addAll(pageResponse.body().getContent());
                        return list;
                    })
                    .map(list -> report()
                            .columns(col.column("전형", "admissionNm", type.stringType()),
                                    col.column("계열", "typeNm", type.stringType()),
                                    col.column("모집단위", "deptNm", type.stringType()),
                                    col.column("시험시간", "attendTime", type.timeHourToSecondType()),
                                    col.column("지원자수", "examineeCnt", type.longType()),
                                    col.column("응시자수", "attendCnt", type.longType()),
                                    col.column("응시율", "attendPer", type.longType()),
                                    col.column("결시자수", "absentCnt", type.longType()),
                                    col.column("결시율", "absentPer", type.longType())
                            ).setDataSource(new JRBeanCollectionDataSource(list)))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.newThread())
                    .subscribe(
                            jasperReportBuilder -> toXlsx(asyncRes, jasperReportBuilder, "모집단위별 응시율")
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

            Observable.range(0, Integer.MAX_VALUE)
                    .concatMap(currentPage -> apiService.statusExaminee(query, currentPage, Integer.MAX_VALUE, sort))
                    .takeUntil(pageResponse -> pageResponse.body().isLast())
                    .reduce(new ArrayList<>(), (list, pageResponse) -> {
                        list.addAll(pageResponse.body().getContent());
                        return list;
                    })
                    .map(list -> report()
                            .columns(
                                    col.column("전형", "admissionNm", type.stringType()),
                                    col.column("모집단위", "deptNm", type.stringType()),
                                    col.column("전공", "majorNm", type.stringType()),
                                    col.column("수험번호", "examineeCd", type.stringType()),
                                    col.column("수험생", "examineeNm", type.stringType()),
                                    col.column("시험일자", "attendDate", type.dateType()),
                                    col.column("시험시간", "attendTime", type.timeHourToSecondType()),
                                    col.column("응시여부", "isAttend", type.booleanType())
                            ).setDataSource(new JRBeanCollectionDataSource(list))
                    )
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.newThread())
                    .subscribe(
                            jasperReportBuilder -> toXlsx(asyncRes, jasperReportBuilder, "수험생별 응시현황")
                            , Throwable::printStackTrace
                            , async::complete
                    );
        });
    }

    private void toXlsx(HttpServletResponse response, JasperReportBuilder report, String title) {
        report.ignorePageWidth()
                .ignorePagination()
                .setPageMargin(DynamicReports.margin(0))
                .addProperty(JasperProperty.EXPORT_XLS_SHEET_NAMES_PREFIX, title);

        response.setHeader("Content-Disposition", FileNameEncoder.encode(title) + ".xlsx");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setHeader("X-Frame-Options", " SAMEORIGIN");
        response.setContentType(TYPE_XLSX);
        try {
            report.toXlsx(response.getOutputStream());
        } catch (DRException | IOException e) {
            e.printStackTrace();
        }
    }
}
