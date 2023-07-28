package com.example.lab5_lab6;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPI {

    @POST("users/user")
    Call<User> post(@Body User user);

    @PUT("users/{id}")
    Call<User> edit(@Path("id")String id, @Body User user);

    @DELETE("users/{id}")
    Call<User> delete (@Path("id")String id);


    @GET("users")
    Call<List<User>> getAllData();
}
