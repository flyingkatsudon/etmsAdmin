package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.ApiService;
import com.humane.util.file.FileNameEncoder;
import com.humane.util.jqgrid.JqgridMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.constant.JasperProperty;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rx.Observable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

@Controller
@RequestMapping("export")
@Slf4j
public class ExportController {
    private final String TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final ApiService apiService;

    @Autowired
    public ExportController(ApiService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping("attend")
    public void attend(@RequestParam(value = "filters", required = false) String filters,
                       @RequestParam(value = "sidx", required = false) String sidx,
                       @RequestParam(value = "sord", required = false) String sord,
                       HttpServletResponse response) throws IOException, DRException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> apiService.statusAttend(query, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                })
                //.subscribeOn(Schedulers.computation())
                //.observeOn(Schedulers.newThread())
                .subscribe(list -> {
                    JasperReportBuilder report = report()
                            .columns(col.column("구분", "admissionNm", type.stringType()),
                                    col.column("전형", "attendTypeNm", type.stringType()),
                                    col.column("응시율", "attendPer", type.longType()),
                                    col.column("지원자", "examineeCnt", type.longType()),
                                    col.column("응시자", "attendCnt", type.longType()),
                                    col.column("결시자", "absentCnt", type.longType()))
                            .setDataSource(new JRBeanCollectionDataSource(list));

                    toXlsx(report, response, "전형별 응시율");
                });
    }

    @RequestMapping("examinee")
    public void examinee(@RequestParam(value = "filters", required = false) String filters,
                         @RequestParam(value = "sidx", required = false) String sidx,
                         @RequestParam(value = "sord", required = false) String sord,
                         HttpServletResponse response) throws IOException, DRException {

        String query = JqgridMapper.getQueryString(filters);
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> apiService.statusExaminee(query, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                })
                //.subscribeOn(Schedulers.computation())
                //.observeOn(Schedulers.newThread())
                .subscribe(list -> {
                    JasperReportBuilder report = report()
                            .columns(
                                    col.column("구분", "admissionNm", type.stringType()),
                                    col.column("모집단위", "majorNm", type.stringType()),
                                    col.column("전공", "deptNm", type.stringType()),
                                    col.column("수험번호", "examineeCd", type.stringType()),
                                    col.column("수험생", "examineeNm", type.stringType()),
                                    col.column("시험일자", "attendDate", type.dateType()),
                                    col.column("시험시간", "attendTime", type.timeHourToSecondType()),
                                    col.column("응시여부", "isAttend", type.booleanType())
                            )
                            .setDataSource(new JRBeanCollectionDataSource(list));

                    toXlsx(report, response, "수험생별 응시현황");
                });
    }

    private void toXlsx(JasperReportBuilder report, HttpServletResponse response, String title) {
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
