package com.humane.etms.service;

import com.humane.etms.mapper.DataMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.LIGHT_GRAY;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataService {
    private StyleBuilder boldStyle = DynamicReports.stl.style().bold();
    private StyleBuilder boldCenteredStyle = DynamicReports.stl.style(boldStyle);

    private StyleBuilder columnTitleStyle = DynamicReports.stl.style(boldCenteredStyle)
            .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
            .setPadding(2);

    private StyleBuilder columnHeaderStyle = DynamicReports.stl.style()
            .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
            .setBorder(DynamicReports.stl.penThin()).setBackgroundColor(LIGHT_GRAY);

    private StyleBuilder columnStyle = DynamicReports.stl.style()
            .setBorder(DynamicReports.stl.penThin())
            .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
    private final DataMapper mapper;

    public Object getExamineeModel() {
        // 기본 생성
        List<ColModel> colModels = new ArrayList<>();
        colModels.add(new ColModel("admissionNm", "전형"));
        colModels.add(new ColModel("typeNm", "계열"));
        colModels.add(new ColModel("examDate", "시험일자"));
        colModels.add(new ColModel("examTime", "시험시간"));
        colModels.add(new ColModel("deptNm", "모집단위"));
        colModels.add(new ColModel("majorNm", "전공"));
        colModels.add(new ColModel("examineeCd", "수험번호"));
        colModels.add(new ColModel("examineeNm", "수험생명"));
        colModels.add(new ColModel("birth", "생년월일"));
        colModels.add(new ColModel("headNm", "고사본부"));
        colModels.add(new ColModel("bldgNm", "고사건물"));
        colModels.add(new ColModel("hallNm", "고사실"));

        // 평가항목 양식에서 답안지 매수 가져오기
        // long itemCnt = mapper.getItemCnt();
        for (int i = 1; i <= 2; i++)
            colModels.add(new ColModel("finalPaperCd1" + i, "답안지" + i));

        return colModels;
    }

    public JasperReportBuilder getExamineeReport() {
        JasperReportBuilder report = report()
                .title(cmp.text("수험생 별 종합").setStyle(columnTitleStyle))
                .columns(
                        col.reportRowNumberColumn("번호").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("전형", "admissionNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("계열", "typeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("시험일자", "examDate", type.dateType()).setPattern("yyyy-MM-dd").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("시험시간", "examTime", type.dateType()).setPattern("HH:mm:ss").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("모집단위", "deptNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("전공", "majorNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("고사본부", "headNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("고사건물", "bldgNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("고사실", "hallNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("가번호", "virtNo", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7)
                )
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);

        return report;

    }

    @Data
    private class ColModel {
        private String name;
        private String label;
        private boolean sortable = true;

        public ColModel(String name, String label) {
            this.name = name;
            this.label = label;
        }

        public ColModel(String name, String label, boolean sortable) {
            this(name, label);
            this.sortable = sortable;
        }
    }
}
