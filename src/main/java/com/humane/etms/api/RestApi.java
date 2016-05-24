package com.humane.etms.api;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.StatusDto;
import com.humane.util.spring.PageResponse;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

import java.util.List;
import java.util.Map;

public interface RestApi {

    @GET("status/all")
    Observable<Response<StatusDto>> all(@QueryMap Map<String, Object> params, @Query("page") String... sort);

    @GET("status/attend")
    Observable<Response<PageResponse<StatusDto>>> attend(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/dept")
    Observable<Response<PageResponse<StatusDto>>> dept(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/hall")
    Observable<Response<PageResponse<StatusDto>>> hall(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/group")
    Observable<Response<PageResponse<StatusDto>>> group(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/examinee")
    Observable<Response<PageResponse<ExamineeDto>>> examinee(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/toolbar")
    Observable<Response<List<StatusDto>>> toolbar(@QueryMap Map<String, Object> parameterMap);

    @GET("image/examinee/{fileName}")
    Observable<Response<ResponseBody>> imageExaminee(@Path("fileName") String fileName);

    /**
     * 넘겨받은 파라미터들로 쿼리를 작성해 감독관 서명 정보를 요청한 후 값을 저장함
     */
    @GET("status/signature")
    Observable<Response<PageResponse<StatusDto>>> signature(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/paper")
    Observable<Response<PageResponse<ExamineeDto>>> paper(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/send")
    Observable<Response<PageResponse<StatusDto>>> send(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);

    @GET("status/device")
    Observable<Response<PageResponse<StatusDto>>> device(@QueryMap Map<String, Object> parameterMap, @Query("page") int page, @Query("size") int size, @Query("sort") String... sort);
}