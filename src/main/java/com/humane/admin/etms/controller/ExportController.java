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
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import retrofit2.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
    public void attend(HttpServletResponse response) throws IOException, DRException {

        Response<ResponseBody> res = apiService.statusAttend(null, 0, Integer.MAX_VALUE).execute();
        if (res.isSuccessful()) {
            List list = JqgridMapper.getResponse(res.body().byteStream()).getRows();

            JasperReportBuilder report = report()
                    //.setTemplate(Templates.reportTemplate)
                    .columns(
                            col.column("구분", "admissionNm", type.stringType()),
                            col.column("전형", "attendTypeNm", type.stringType()),
                            col.column("응시율", "attendPer", type.bigDecimalType()),
                            col.column("지원자", "examineeCnt", type.integerType()),
                            col.column("응시자", "attendCnt", type.integerType()),
                            col.column("결시자", "absentCnt", type.integerType())
                    )
                    //.pageFooter(Templates.footerComponent)
                    //.title(Templates.createTitleComponent("DynamicPageWidth"))
                    .setDataSource(new JRBeanCollectionDataSource(list));

            toXlsx(report, response, "전형별 응시율");
        }
    }

    private void toXlsx(JasperReportBuilder report, HttpServletResponse response, String title) throws IOException, DRException {
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
