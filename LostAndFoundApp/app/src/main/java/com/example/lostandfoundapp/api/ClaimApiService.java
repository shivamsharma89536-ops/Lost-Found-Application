package com.example.lostandfoundapp.api;

import com.example.lostandfoundapp.models.Claim;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ClaimApiService {

    @GET("api/claims")
    Call<List<Claim>> getMyClaims();

}