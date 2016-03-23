package com.humane.admin.etms.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/admission")
    Call<ResponseBody> admission(@Query("q") String q, @Query("page") int page, @Query("size") int size);

    @GET("api/hall")
    Call<ResponseBody> hall(@Query("q") String q, @Query("page") int page, @Query("size") int size);

    @GET("api/examinee")
    Call<ResponseBody> examinee(@Query("q") String q, @Query("page") int page, @Query("size") int size);

    @GET("api/attendMap")
    Call<ResponseBody> attendMap(@Query("q") String q, @Query("page") int page, @Query("size") int size);

    @GET("api/attendHall")
    Call<ResponseBody> attendHall(@Query("q") String q, @Query("page") int page, @Query("size") int size);

    @GET("api/image/examinee/{fileName}")
    Call<ResponseBody> imageExaminee(@Path("fileName") String fileName);
}