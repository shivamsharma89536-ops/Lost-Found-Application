package com.example.lostandfoundapp.api;

import com.example.lostandfoundapp.models.LoginRequest;
import com.example.lostandfoundapp.models.LoginResponse;
import com.example.lostandfoundapp.models.RegisterRequest;
import com.example.lostandfoundapp.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PATCH;

public interface AuthApiService {

    @POST("auth/register")
    Call<User> registerUser(@Body RegisterRequest registerRequest);

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("api/users/me")
    Call<User> getCurrentUser();


    @PATCH("users/me")
    Call<User> updateUser(@Body User user);
}