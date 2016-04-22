package com.humane.admin.etms.controller;

import com.humane.admin.etms.api.RestApi;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.admin.etms.service.ExportService;
import com.humane.util.ObjectConvert;
import com.humane.util.jqgrid.JqgridPager;
import com.humane.util.spring.PageResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;
import rx.Observable;

@RestController
@RequestMapping(value = "status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StatusController {
    private final RestApi restApi;
    private final ExportService exportService;

    @RequestMapping(value = "attend")
    public ResponseEntity attend(StatusDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.statusAttend(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort()
        );
        return exportService.toJqgrid(observable);
    }

    @RequestMapping(value = "dept")
    public ResponseEntity dept(StatusDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.statusDept(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort()
        );
        return exportService.toJqgrid(observable);
    }

    @RequestMapping(value = "hall")
    public ResponseEntity hall(StatusDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.statusHall(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort()
        );
        return exportService.toJqgrid(observable);
    }

    @RequestMapping(value = "group")
    public ResponseEntity group(StatusDto statusDto, JqgridPager pager) {
        Observable<Response<PageResponse<StatusDto>>> observable = restApi.statusGroup(
                ObjectConvert.asMap(statusDto),
                pager.getPage() - 1,
                pager.getRows(),
                pager.getSort()
        );
        return exportService.toJqgrid(observable);
    }
}