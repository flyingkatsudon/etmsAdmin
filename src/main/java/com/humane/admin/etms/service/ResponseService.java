package com.humane.admin.etms.service;

import com.humane.admin.etms.dto.ChartJsDto;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.util.jqgrid.JqgridMapper;
import com.humane.util.spring.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import rx.Observable;

import java.util.List;

@Slf4j
@Component
public class ResponseService {
    public ResponseEntity toJqgridResponseEntity(Observable<Response<PageResponse<StatusDto>>> observable) {
        try {
            Response<PageResponse<StatusDto>> res = observable.toBlocking().first();
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

    public ResponseEntity toResponseEntity(Observable<Response<List<StatusDto>>> observable) {

        try {
            Response<List<StatusDto>> res = observable.toBlocking().first();
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
}
