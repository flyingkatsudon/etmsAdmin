package com.humane.admin.etms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.admin.etms.api.ApiService;
import com.humane.admin.etms.dto.ChartJsResponse;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.query.QueryBuilder;
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
import java.util.Map;

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
    public ResponseEntity<ChartJsResponse> attend(
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "1000") int rows
    ) throws IOException {

        String query = null;
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusAttend(query, page - 1, rows, sort).execute();
        if (response.isSuccessful()) {
            ChartJsResponse chartJsResponse = new ChartJsResponse();
            ChartJsResponse.Dataset attendDataset = new ChartJsResponse.Dataset("응시율");
            ChartJsResponse.Dataset absentDataset = new ChartJsResponse.Dataset("결시율");
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<PageResponse<StatusDto>> typeRef = new TypeReference<PageResponse<StatusDto>>() {
            };
            PageResponse<StatusDto> pageResponse = mapper.readValue(response.body().bytes(), typeRef);

            pageResponse.getContent().forEach(statusDto -> {
                chartJsResponse.addLabel(statusDto.getAttendNm());
                attendDataset.addData(statusDto.getAttendCnt());
                absentDataset.addData(statusDto.getAbsentCnt());
            });

            chartJsResponse.addDataset(attendDataset);
            chartJsResponse.addDataset(absentDataset);
            return ResponseEntity.ok(chartJsResponse);
        }

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "major", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ChartJsResponse> major(
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "1000") int rows
    ) throws IOException {

        String query = null;
        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusMajor(query, page - 1, rows, sort).execute();
        if (response.isSuccessful()) {
            ChartJsResponse chartJsResponse = new ChartJsResponse();
            ChartJsResponse.Dataset attendDataset = new ChartJsResponse.Dataset("응시율");
            ChartJsResponse.Dataset absentDataset = new ChartJsResponse.Dataset("결시율");
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<PageResponse<StatusDto>> typeRef = new TypeReference<PageResponse<StatusDto>>() {
            };
            PageResponse<StatusDto> pageResponse = mapper.readValue(response.body().bytes(), typeRef);

            pageResponse.getContent().forEach(statusDto -> {
                chartJsResponse.addLabel(statusDto.getMajorNm());
                attendDataset.addData(statusDto.getAttendCnt());
                absentDataset.addData(statusDto.getAbsentCnt());
            });

            chartJsResponse.addDataset(attendDataset);
            chartJsResponse.addDataset(absentDataset);
            return ResponseEntity.ok(chartJsResponse);
        }

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }

    @RequestMapping(value = "dept", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ChartJsResponse> dept(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sidx", required = false) String sidx,
            @RequestParam(value = "sord", required = false) String sord,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "rows", required = false, defaultValue = "1000") int rows
    ) throws IOException {


        QueryBuilder queryBuilder = new QueryBuilder();
        String query = null;
        if (q != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> a = objectMapper.readValue(q, new TypeReference<Map>() {
            });
            a.entrySet().forEach(o -> {
                String key = o.getKey();
                String value = o.getValue();
                if (!value.isEmpty()) queryBuilder.add(key, value);
            });
            query = queryBuilder.build();
        }

        String[] sort = JqgridMapper.getSortString(sidx, sord);

        Response<ResponseBody> response = apiService.statusDept(query, page - 1, rows, sort).execute();
        if (response.isSuccessful()) {
            ChartJsResponse chartJsResponse = new ChartJsResponse();
            ChartJsResponse.Dataset attendCnt = new ChartJsResponse.Dataset("응시자");
            ChartJsResponse.Dataset attendPer = new ChartJsResponse.Dataset("응시율");
            ChartJsResponse.Dataset absentCnt = new ChartJsResponse.Dataset("결시자");
            ChartJsResponse.Dataset absentPer = new ChartJsResponse.Dataset("결시율");
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<PageResponse<StatusDto>> typeRefStatus = new TypeReference<PageResponse<StatusDto>>() {
            };
            PageResponse<StatusDto> pageResponse = mapper.readValue(response.body().bytes(), typeRefStatus);

            pageResponse.getContent().forEach(statusDto -> {
                chartJsResponse.addLabel(statusDto.getDeptNm());
                attendCnt.addData(statusDto.getAttendCnt() == null ? 0 : statusDto.getAttendCnt());
                attendPer.addData(statusDto.getAttendPer() == null ? 0 : statusDto.getAttendPer());
                absentCnt.addData(statusDto.getAbsentCnt() == null ? 0 : statusDto.getAbsentCnt());
                absentPer.addData(statusDto.getAbsentPer() == null ? 0 : statusDto.getAbsentPer());
            });

            chartJsResponse.addDataset(attendCnt);
            chartJsResponse.addDataset(attendPer);
            chartJsResponse.addDataset(absentCnt);
            chartJsResponse.addDataset(absentPer);
            return ResponseEntity.ok(chartJsResponse);
        }

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(null);
    }
}
