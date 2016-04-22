package com.humane.admin.etms.service;

import com.humane.admin.etms.api.RestApi;
import com.humane.admin.etms.dto.StatusDto;
import lombok.RequiredArgsConstructor;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportService {
    private final RestApi restApi;

    public Observable<ArrayList<Object>> allAttend(Map<String, String> query, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.statusAttend(query, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
    }

    public Observable<JasperReportBuilder> reportAttend(Map<String, String> query, String... sort) {
        return allAttend(query, sort)
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

    public Observable<ArrayList<Object>> allDept(Map<String, String> query, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.statusDept(query, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
    }

    public Observable<JasperReportBuilder> reportDept(Map<String, String> query, String... sort) {
        return allDept(query, sort)
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

    public Observable<ArrayList<Object>> allHall(Map<String, String> query, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.statusHall(query, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
    }

    public Observable<JasperReportBuilder> reportHall(Map<String, String> query, String... sort) {
        return allHall(query, sort)
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

    public Observable<ArrayList<Object>> allGroup(Map<String, String> query, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.statusGroup(query, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
    }

    public Observable<JasperReportBuilder> reportGroup(Map<String, String> query, String... sort) {
        return allGroup(query, sort)
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
                .concatMap(currentPage -> restApi.statusExaminee(query, currentPage, Integer.MAX_VALUE, sort))
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


    public JasperPrint getPrint(String path, HashMap<String, Object> param, List<StatusDto> list) {

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
            return JasperFillManager.fillReport(jasperReport, param, new JRBeanCollectionDataSource(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
