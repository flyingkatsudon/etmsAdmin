package com.humane.admin.etms.service;

import com.humane.admin.etms.api.RestApi;
import com.humane.admin.etms.dto.ExamineeDto;
import com.humane.admin.etms.dto.StatusDto;
import com.humane.util.spring.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiService {
    private final RestApi restApi;

    public Observable<Response<PageResponse<StatusDto>>> attend(
            Map<String, String> params,
            int page,
            int rows,
            String... sort) {
        return restApi.attend(params, page, rows, sort);
    }

    public Observable<List<StatusDto>> attend(
            Map<String, String> params,
            String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.attend(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
    }

    public Observable<Response<PageResponse<StatusDto>>> dept(
            Map<String, String> params,
            int page,
            int rows,
            String... sort
    ) {
        return restApi.dept(params, page, rows, sort);
    }

    public Observable<List<StatusDto>> dept(
            Map<String, String> params,
            String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.dept(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
    }

    public Observable<Response<PageResponse<StatusDto>>> hall(
            Map<String, String> params,
            int page,
            int rows,
            String... sort
    ) {
        return restApi.hall(params, page, rows, sort);
    }

    public Observable<List<StatusDto>> hall(
            Map<String, String> params, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.hall(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });

    }

    public Observable<Response<PageResponse<StatusDto>>> group(
            Map<String, String> params,
            int page,
            int rows,
            String... sort
    ) {
        return restApi.group(params, page, rows, sort);
    }

    public Observable<List<StatusDto>> group(
            Map<String, String> params, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.group(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
    }

    public Observable<Response<PageResponse<ExamineeDto>>> examinee(
            Map<String, String> params,
            int page,
            int rows,
            String... sort
    ) {
        return restApi.examinee(params, page, rows, sort);
    }

    public Observable<List<ExamineeDto>> examinee(
            Map<String, String> params, String... sort) {
        return Observable.range(0, Integer.MAX_VALUE)
                .concatMap(currentPage -> restApi.examinee(params, currentPage, Integer.MAX_VALUE, sort))
                .takeUntil(pageResponse -> pageResponse.body().isLast())
                .reduce(new ArrayList<>(), (list, pageResponse) -> {
                    list.addAll(pageResponse.body().getContent());
                    return list;
                });
    }
}
