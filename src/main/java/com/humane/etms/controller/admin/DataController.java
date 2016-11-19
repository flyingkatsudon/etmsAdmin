package com.humane.etms.controller.admin;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.DataMapper;
import com.humane.etms.service.ImageService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.awt.Color.LIGHT_GRAY;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

@RestController
@RequestMapping(value = "data")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataController {
    private final DataMapper mapper;
    private final ImageService imageService;
    private static final String JSON = "json";
    private static final String PDF = "pdf";

    public static StyleBuilder columnHeaderStyle = DynamicReports.stl.style()
            .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)
            .setBorder(DynamicReports.stl.penThin()).setBackgroundColor(LIGHT_GRAY);

    public static StyleBuilder columnStyle = DynamicReports.stl.style()
            .setBorder(DynamicReports.stl.penThin())
            .setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);

    @Value("${path.image.examinee:C:/api/image/examinee}") String pathExaminee;
    @Value("${path.image.univLogo:C:/api/image/univLogo}") String pathUnivLogo;

    @RequestMapping(value = "examinee.{format:json|pdf|xls|xlsx}")
    public ResponseEntity examinee(@PathVariable String format, StatusDto statusDto, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.examinee(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-examinee.jrxml",
                        format,
                        mapper.examinee(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "examineeId.pdf")
    public ResponseEntity examineeId(ExamineeDto examineeDto, Pageable pageable) {
        List<ExamineeDto> list = mapper.examinee(examineeDto, pageable).getContent();

        list.forEach(item -> {
            try (InputStream is = imageService.getFile(pathExaminee, item.getExamineeCd() + ".jpg")) {
                BufferedImage image = ImageIO.read(is);
                item.setExamineeImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (InputStream is = imageService.getFile(pathUnivLogo, "univLogo.png")) {
                BufferedImage image = ImageIO.read(is);
                item.setUnivLogo(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/examinee-id-card.jrxml",
                JasperReportsExportHelper.EXT_PDF,
                list
        );
    }

    @RequestMapping(value = "noIdCard.{format:json|pdf|xls|xlsx}")
    public ResponseEntity noIdCard(@PathVariable String format, ExamineeDto examineeDto, Pageable pageable) {
        examineeDto.setIsNoIdCard(true);
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.examinee(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-noIdCard.jrxml",
                        format,
                        mapper.examinee(examineeDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "recheck.{format:json|pdf|xls|xlsx}")
    public ResponseEntity recheck(@PathVariable String format, ExamineeDto examineeDto, Pageable pageable) {
        examineeDto.setIsCheck(true);
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.examinee(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-recheck.jrxml",
                        format,
                        mapper.examinee(examineeDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "otherHall.{format:json|pdf|xls|xlsx}")
    public ResponseEntity otherHall(@PathVariable String format, ExamineeDto examineeDto, Pageable pageable) {
        examineeDto.setIsOtherHall(true);

        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.examinee(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-otherHall.jrxml",
                        format,
                        mapper.examinee(examineeDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    /**
     * 감독관 서명 정보를 불러오기 위한 부분
     *
     * @param format    요청할 형태
     * @param statusDto 상태 정보
     * @param pageable
     * @return
     */
    @RequestMapping(value = "signature.{format:json|xlsx}")
    public ResponseEntity signature(@PathVariable String format, StatusDto statusDto, Pageable pageable) {

        switch (format) {
            case JSON:
                // 감독관 서명 정보를 요청함
                return ResponseEntity.ok(mapper.signature(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-signature.jrxml",
                        format,
                        mapper.signature(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "paper.{format:json|xlsx}")
    public ResponseEntity paper(@PathVariable String format, ExamineeDto statusDto, Pageable pageable) {

        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.paper(statusDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-paper.jrxml",
                        format,
                        mapper.paper(statusDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "detail.{format:json|pdf}")
    public ResponseEntity detail(@PathVariable String format, StatusDto param, Pageable pageable) {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.detail(param, pageable));
            case PDF:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/data-detail.jrxml"
                        , format
                        , mapper.detail(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
            default:
                return null;
        }
    }

    @RequestMapping(value = "checkIdCard")
    public String checkIdCard(String examineeCd, String attendCd) {
        Date idCheckDttm = new Date();
        mapper.checkIdCard(examineeCd, idCheckDttm, attendCd);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(idCheckDttm);
    }

    @RequestMapping(value = "reCheck")
    public String reCheck(String examineeCd, String attendCd) {
        Date recheckDttm = new Date();
        mapper.recheck(examineeCd, recheckDttm, attendCd);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recheckDttm);
    }
/*

    @RequestMapping(value = "sendPaperInfo.{format:xls|xlsx}")
    public ResponseEntity sendPaperInfo(@PathVariable String format, ExamineeDto param, Pageable pageable) throws DRException {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/data-sendPaperInfo.jrxml"
                , format
                , mapper.sendPaperInfo(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
        );
    }
*/

    @RequestMapping(value = "sendPaperInfo.{format:xls|xlsx}")
    public ResponseEntity sendPaperInfo(@PathVariable String format, ExamineeDto param, Pageable pageable) throws DRException {
        JasperReportBuilder report= report()
                .columns(
                        col.column("수험번호", "examineeCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("성명", "examineeNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("결시여부", "attendYn", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("시험일자", "attendDate", type.dateType()).setPattern("yyyy-MM-dd").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("교시", "attendTime", type.dateType()).setPattern("HH:mm").setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(5),
                        col.column("바코드", "paperCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("배정고사실코드", "hallCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("배정고사실명", "hallNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7),
                        col.column("실제고사실코드", "attendHallCd", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(8),
                        col.column("실제고사실명", "attendHallNm", type.stringType()).setTitleStyle(columnHeaderStyle).setStyle(columnStyle).setFixedColumns(7)
                )
                .setPageMargin(DynamicReports.margin(0))
                .setIgnorePageWidth(true)
                .setIgnorePagination(true);

        report.setDataSource(mapper.sendPaperInfo(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());

        JasperPrint jasperPrint = report.toJasperPrint();
        jasperPrint.setName("답안지매칭 전달양식");

        return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
    }

    @RequestMapping(value = "cancelAttend.{format:xls|xlsx}")
    public ResponseEntity cancelAttend(@PathVariable String format, StatusDto param, Pageable pageable) throws DRException {
        param.setIsCancelAttend(true);
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/data-cancelAttend.jrxml"
                , format
                , mapper.cancelAttend(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
        );
    }

    @RequestMapping(value = "sqlEdit.{format:json|xls|xlsx}")
    public ResponseEntity sqlEdit(@PathVariable String format, @RequestParam(value = "sql") String sql) throws DRException {
        switch (format) {
            case JSON:
                return ResponseEntity.ok(mapper.sqlEdit(sql));
            default:
                List<Map<String, String>> list = mapper.sqlEdit(sql);

                for (Map<String, String> map : list) {
                    Set<String> keyset = map.keySet();
                    for (String key : keyset) {
                        Object value = map.get(key);
                        map.put(key, String.valueOf(value == null ? "" : String.valueOf(value)));
                    }
                }

                JasperReportBuilder report = report()
                        .setPageMargin(DynamicReports.margin(0))
                        .setIgnorePageWidth(true)
                        .setIgnorePagination(true);

                Set<String> keyset = list.get(0).keySet();
                for (String key : keyset) {
                    report.addColumn(
                            col.column(key, key, type.stringType())
                                    .setTitleStyle(columnHeaderStyle)
                                    .setStyle(columnStyle)
                                    .setFixedColumns(7)
                    );
                }

                report.setDataSource(list);
                JasperPrint jasperPrint = report.toJasperPrint();
                return JasperReportsExportHelper.toResponseEntity(jasperPrint, format);
        }
    }
}