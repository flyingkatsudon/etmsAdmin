package com.humane.admin.etms.service;

import com.humane.admin.etms.dto.ChartJsDto;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.util.file.FileNameEncoder;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.spring.PageResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.constant.JasperProperty;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import rx.Observable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Slf4j
@Component
public class ExportService {
    private void addHeaderXlsx(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", FileNameEncoder.encode(fileName) + ".xlsx");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setHeader("X-Frame-Options", " SAMEORIGIN");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    private void addHeaderPdf(HttpServletResponse response, String title) {
        response.setHeader("Content-Disposition", "inline;attachment; filename=" + FileNameEncoder.encode(title) + ".pdf");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setHeader("X-Frame-Options", " SAMEORIGIN");
        response.setContentType("application/pdf");
    }

    public <T> ResponseEntity toJqgrid(Observable<Response<PageResponse<T>>> observable) {
        try {
            Response<PageResponse<T>> res = observable.toBlocking().first();
            if (res.isSuccessful()) {
                return ResponseEntity.ok(JqgridMapper.getResponse(res.body()));
            } else {
                log.error("{}", res.errorBody());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
            }
        } catch (Throwable e) {
            log.debug("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    public <T> ResponseEntity toResponseEntity(Observable<Response<List<T>>> observable) {

        try {
            Response<List<T>> res = observable.toBlocking().first();
            if (res.isSuccessful()) return ResponseEntity.ok(res.body());
            else {
                log.error("{}", res.errorBody());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
            }
        } catch (Throwable e) {
            log.debug("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    public ResponseEntity<ChartJsDto> toChart(Observable<Response<PageResponse<StatusDto>>> observable, String typeNm) {

        try {
            Response<PageResponse<StatusDto>> res = observable.toBlocking().first();
            if (res.isSuccessful()) return ResponseEntity.ok(toChart(typeNm, res.body().getContent()));
            else {
                log.error("{}", res.errorBody());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
            }
        } catch (Throwable e) {
            log.debug("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    private ChartJsDto toChart(String fieldName, List<StatusDto> content) {
        ChartJsDto chartJsDto = new ChartJsDto();
        ChartJsDto.Dataset attendCnt = new ChartJsDto.Dataset("응시자수");
        ChartJsDto.Dataset attendPer = new ChartJsDto.Dataset("응시율");
        ChartJsDto.Dataset absentCnt = new ChartJsDto.Dataset("결시자수");
        ChartJsDto.Dataset absentPer = new ChartJsDto.Dataset("결시율");
        content.forEach(statusDto -> {
            try {
                chartJsDto.addLabel(FieldUtils.readField(statusDto, fieldName, true).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            attendCnt.addData(statusDto.getAttendCnt() == null ? 0 : statusDto.getAttendCnt());
            attendPer.addData(statusDto.getAttendPer() == null ? 0 : statusDto.getAttendPer());
            absentCnt.addData(statusDto.getAbsentCnt() == null ? 0 : statusDto.getAbsentCnt());
            absentPer.addData(statusDto.getAbsentPer() == null ? 0 : statusDto.getAbsentPer());
        });
        chartJsDto.addDataset(attendCnt);
        chartJsDto.addDataset(attendPer);
        chartJsDto.addDataset(absentCnt);
        chartJsDto.addDataset(absentPer);

        return chartJsDto;
    }

    public void toPdf(HttpServletResponse response, JasperPrint jasperPrint, String title) {
        addHeaderPdf(response, title);
        try (OutputStream os = response.getOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, os);
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
    }

    public void toXlsx(HttpServletResponse response, JasperReportBuilder report, String title) {
        report.ignorePageWidth()
                .ignorePagination()
                .setPageMargin(DynamicReports.margin(0))
                .addProperty(JasperProperty.EXPORT_XLS_SHEET_NAMES_PREFIX, title);

        try (OutputStream out = response.getOutputStream()) {
            addHeaderXlsx(response, title);
            report.toXlsx(out);
        } catch (IOException | DRException e) {
            e.printStackTrace();
        }
    }

    public void toXlsx(HttpServletResponse response, JasperPrint jasperPrint, String title) {
        try (OutputStream os = response.getOutputStream()) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));

            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            exporter.setConfiguration(configuration);

            addHeaderXlsx(response, title);
            exporter.exportReport();
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
    }
}
