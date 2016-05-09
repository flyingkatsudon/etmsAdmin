package com.humane.admin.etms.controller;

import com.humane.admin.etms.dto.StatusDto;
import com.humane.admin.etms.service.ApiService;
import com.humane.util.ObjectConvert;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.jqgrid.JqgridPager;
import com.humane.util.spring.PageResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "check")
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CheckController {
    private static final String CHART = "chart";
    private static final String LIST = "list";

    private final ApiService apiService;

    @RequestMapping(value = "send/{format:list|xls|xlsx}")
    public ResponseEntity send(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.send(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-send.jrxml",
                        format,
                        apiService.send(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }

    @RequestMapping(value = "device/{format:list|xls|xlsx}")
    public ResponseEntity device(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.device(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-device.jrxml",
                        format,
                        apiService.device(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }

    @RequestMapping(value = "detect/{format:list|chart|pdf|xls|xlsx}")
    public ResponseEntity detect(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {

        return null;
    }

    @RequestMapping(value = "signature/{format:list|xls|xlsx}")
    public ResponseEntity signature(@PathVariable String format, StatusDto statusDto, JqgridPager pager, HttpServletResponse response) {
        statusDto.setIsSignature(false);
        switch (format) {
            case LIST:
                Response<PageResponse<StatusDto>> pageResponse = apiService.signature(
                        ObjectConvert.asMap(statusDto),
                        pager.getPage() - 1,
                        pager.getRows(),
                        pager.getSort()
                );
                return ResponseEntity.ok(JqgridMapper.getResponse(pageResponse.body()));
            default:
                return JasperReportsExportHelper.toResponseEntity(response,
                        "jrxml/check-signature.jrxml",
                        format,
                        apiService.signature(ObjectConvert.asMap(statusDto), pager.getSort())
                );
        }
    }
}