package com.humane.admin.etms.api;

import com.humane.admin.etms.dto.StatusDto;
import com.humane.util.spring.PageResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import java.util.List;

public interface ApiService {

    @GET("api/status/attend")
    Observable<Response<PageResponse<StatusDto>>> statusAttend(@Query("q") String q, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("api/status/dept")
    Observable<Response<PageResponse<StatusDto>>> statusDept(@Query("q") String q, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("api/status/hall")
    Observable<Response<PageResponse<StatusDto>>> statusHall(@Query("q") String q, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("api/status/group")
    Observable<Response<PageResponse<StatusDto>>> statusGroup(@Query("q") String q, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("api/status/examinee")
    Observable<Response<PageResponse<StatusDto>>> statusExaminee(@Query("q") String q, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("api/status/toolbar")
    Observable<Response<List<StatusDto>>> statusToolbar(@Query("q") String q);

    @GET("api/image/examinee/{fileName}")
    Observable<Response<ResponseBody>> imageExaminee(@Path("fileName") String fileName);
}