package com.example.newsapplication.rests;

import com.example.newsapplication.model.ResponseModel;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<ResponseModel> getIndianNews(@Query("country") String country, @Query("category") String category , @Query("apiKey") String apiKey );

    @GET("top-headlines")
    Call<ResponseModel> getHomeNews(@Query("country") String country , @Query("apiKey") String apiKey);
}
