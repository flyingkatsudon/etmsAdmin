package com.humane.admin.etms.service;

import com.humane.admin.etms.api.ApiService;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.util.file.FileNameEncoder;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.constant.JasperProperty;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

@Service
public class ExportService {

    private static final String TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Autowired private ApiService apiService;

    public Observable<JasperReportBuilder> reportAttend(Map<String, String> query, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
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
                                col.column("응시율", "attendPer", type.bigDecimalType()),
                                col.column("결시자수", "absentCnt", type.longType()),
                                col.column("결시율", "absentPer", type.bigDecimalType())
                        ).setDataSource(new JRBeanCollectionDataSource(list))
                );
    }

    public Observable<JasperReportBuilder> reportDept(Map<String, String> query, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
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
                                col.column("시험일자", "attendDate", type.dateType()),
                                col.column("시험시간", "attendTime", type.timeHourToSecondType()),
                                col.column("지원자수", "examineeCnt", type.longType()),
                                col.column("응시자수", "attendCnt", type.longType()),
                                col.column("응시율", "attendPer", type.bigDecimalType()),
                                col.column("결시자수", "absentCnt", type.longType()),
                                col.column("결시율", "absentPer", type.bigDecimalType())
                        ).setDataSource(new JRBeanCollectionDataSource(list))
                );
    }


    public Observable<JasperReportBuilder> reportHall(Map<String, String> query, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> apiService.statusHall(query, currentPage, Integer.MAX_VALUE, sort))
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
                                col.column("고사본부", "headNm", type.stringType()),
                                col.column("고사건물", "bldgNm", type.stringType()),
                                col.column("고사실", "hallNm", type.stringType()),
                                col.column("지원자수", "examineeCnt", type.longType()),
                                col.column("응시자수", "attendCnt", type.longType()),
                                col.column("응시율", "attendPer", type.bigDecimalType()),
                                col.column("결시자수", "absentCnt", type.longType()),
                                col.column("결시율", "absentPer", type.bigDecimalType())
                        ).setDataSource(new JRBeanCollectionDataSource(list))
                );
    }

    public Observable<JasperReportBuilder> reportGroup(Map<String, String> query, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> apiService.statusGroup(query, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                })
                .map(list -> report()
                        .columns(
                                col.column("전형", "admissionNm", type.stringType()),
                                col.column("계열", "typeNm", type.stringType()),
                                col.column("시험일자", "attendDate", type.dateType()),
                                col.column("시험시간", "attendTime", type.timeHourToSecondType()),
                                col.column("모집단위", "deptNm", type.stringType()),
                                col.column("전공", "majorNm", type.stringType()),
                                col.column("조", "groupNm", type.stringType()),
                                col.column("지원자수", "examineeCnt", type.longType()),
                                col.column("응시자수", "attendCnt", type.longType()),
                                col.column("응시율", "attendPer", type.bigDecimalType()),
                                col.column("결시자수", "absentCnt", type.longType()),
                                col.column("결시율", "absentPer", type.bigDecimalType())
                        ).setDataSource(new JRBeanCollectionDataSource(list))
                );
    }

    public Observable<ArrayList<StatusDto>> getAllExaminee(Map<String, String> query, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> apiService.statusExaminee(query, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
    }

    public Observable<JasperReportBuilder> reportExaminee(Map<String, String> query, String... sort) {
        return getAllExaminee(query, sort)
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
                );
    }

    public void toXlsx(HttpServletResponse response, JasperReportBuilder report, String title) throws IOException, DRException {
        report.ignorePageWidth()
                .ignorePagination()
                .setPageMargin(DynamicReports.margin(0))
                .addProperty(JasperProperty.EXPORT_XLS_SHEET_NAMES_PREFIX, title);

        response.setHeader("Content-Disposition", FileNameEncoder.encode(title) + ".xlsx");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setHeader("X-Frame-Options", " SAMEORIGIN");
        response.setContentType(TYPE_XLSX);
        report.toXlsx(response.getOutputStream());
    }
}
