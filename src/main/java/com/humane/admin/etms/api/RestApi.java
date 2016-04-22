package com.humane.admin.etms.api;

import com.humane.admin.etms.dto.ExamineeDto;
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

public interface RestApi {

    @GET("status/attend")
    Observable<Response<PageResponse<StatusDto>>> attend(@QueryMap Map<String, String> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/dept")
    Observable<Response<PageResponse<StatusDto>>> dept(@QueryMap Map<String, String> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/hall")
    Observable<Response<PageResponse<StatusDto>>> hall(@QueryMap Map<String, String> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/group")
    Observable<Response<PageResponse<StatusDto>>> group(@QueryMap Map<String, String> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/examinee")
    Observable<Response<PageResponse<ExamineeDto>>> examinee(@QueryMap Map<String, String> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/toolbar")
    Observable<Response<List<StatusDto>>> toolbar(@QueryMap Map<String, String> parameterMap);

    @GET("image/examinee/{fileName}")
    Observable<Response<ResponseBody>> imageExaminee(@Path("fileName") String fileName);
}