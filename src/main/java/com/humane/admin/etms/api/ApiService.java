package com.humane.admin.etms.api;

import com.humane.admin.etms.dto.StatusDto;
import com.humane.util.spring.PageResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

import java.util.List;
import java.util.Map;

public interface ApiService {

    @GET("status/attend")
    Observable<Response<PageResponse<StatusDto>>> statusAttend(@QueryMap Map<String, String> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/dept")
    Observable<Response<PageResponse<StatusDto>>> statusDept(@QueryMap Map<String, String> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/hall")
    Observable<Response<PageResponse<StatusDto>>> statusHall(@QueryMap Map<String, String> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/group")
    Observable<Response<PageResponse<StatusDto>>> statusGroup(@QueryMap Map<String, String> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/examinee")
    Observable<Response<PageResponse<StatusDto>>> statusExaminee(@QueryMap Map<String, String> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/toolbar")
    Observable<Response<List<StatusDto>>> statusToolbar(@QueryMap Map<String, String> parameterMap);

    @GET("image/examinee/{fileName}")
    Observable<Response<ResponseBody>> imageExaminee(@Path("fileName") String fileName);
}