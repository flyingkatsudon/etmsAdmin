package com.humane.admin.etms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.admin.etms.api.ApiService;
import com.humane.admin.etms.dto.ChartJsDto;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.spring.PageResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

import java.io.IOException;

@RestController
@RequestMapping("chart")
@Slf4j
public class ChartController {

    private final ApiService apiService;

    @Autowired
    public ChartController(ApiService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping(value = "attend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ChartJsDto> attend(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "1000") int rows
    ) throws IOException {

        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusAttend(q, page - 1, rows, sort).execute();
        if (response.isSuccessful()) {
            ChartJsDto chartJsDto = new ChartJsDto();
            ChartJsDto.Dataset attendDataset = new ChartJsDto.Dataset("응시율");
            ChartJsDto.Dataset absentDataset = new ChartJsDto.Dataset("결시율");
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<PageResponse<StatusDto>> typeRef = new TypeReference<PageResponse<StatusDto>>() {
            };
            PageResponse<StatusDto> pageResponse = mapper.readValue(response.body().bytes(), typeRef);

            pageResponse.getContent().forEach(statusDto -> {
                chartJsDto.addLabel(statusDto.getAttendNm());
                attendDataset.addData(statusDto.getAttendCnt());
                absentDataset.addData(statusDto.getAbsentCnt());
            });

            chartJsDto.addDataset(attendDataset);
            chartJsDto.addDataset(absentDataset);
            return ResponseEntity.ok(chartJsDto);
        }

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "major", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ChartJsDto> major(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "1000") int rows
    ) throws IOException {

        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusMajor(q, page - 1, rows, sort).execute();
        if (response.isSuccessful()) {
            ChartJsDto chartJsDto = new ChartJsDto();
            ChartJsDto.Dataset attendDataset = new ChartJsDto.Dataset("응시자");
            ChartJsDto.Dataset absentDataset = new ChartJsDto.Dataset("결시자");
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<PageResponse<StatusDto>> typeRef = new TypeReference<PageResponse<StatusDto>>() {
            };
            PageResponse<StatusDto> pageResponse = mapper.readValue(response.body().bytes(), typeRef);

            pageResponse.getContent().forEach(statusDto -> {
                chartJsDto.addLabel(statusDto.getMajorNm());
                attendDataset.addData(statusDto.getAttendCnt());
                absentDataset.addData(statusDto.getAbsentCnt());
            });

            chartJsDto.addDataset(attendDataset);
            chartJsDto.addDataset(absentDataset);
            return ResponseEntity.ok(chartJsDto);
        }

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "dept", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ChartJsDto> dept(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "1000") int rows
    ) throws IOException {

        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusDept(q, page - 1, rows, sort).execute();
        if (response.isSuccessful()) {
            ChartJsDto chartJsDto = new ChartJsDto();
            ChartJsDto.Dataset attendCnt = new ChartJsDto.Dataset("응시자");
            ChartJsDto.Dataset attendPer = new ChartJsDto.Dataset("응시율");
            ChartJsDto.Dataset absentCnt = new ChartJsDto.Dataset("결시자");
            ChartJsDto.Dataset absentPer = new ChartJsDto.Dataset("결시율");
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<PageResponse<StatusDto>> typeRefStatus = new TypeReference<PageResponse<StatusDto>>() {
            };
            PageResponse<StatusDto> pageResponse = mapper.readValue(response.body().bytes(), typeRefStatus);

            pageResponse.getContent().forEach(statusDto -> {
                chartJsDto.addLabel(statusDto.getDeptNm());
                attendCnt.addData(statusDto.getAttendCnt() == null ? 0 : statusDto.getAttendCnt());
                attendPer.addData(statusDto.getAttendPer() == null ? 0 : statusDto.getAttendPer());
                absentCnt.addData(statusDto.getAbsentCnt() == null ? 0 : statusDto.getAbsentCnt());
                absentPer.addData(statusDto.getAbsentPer() == null ? 0 : statusDto.getAbsentPer());
            });

            chartJsDto.addDataset(attendCnt);
            chartJsDto.addDataset(attendPer);
            chartJsDto.addDataset(absentCnt);
            chartJsDto.addDataset(absentPer);
            return ResponseEntity.ok(chartJsDto);
        }

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }
}
