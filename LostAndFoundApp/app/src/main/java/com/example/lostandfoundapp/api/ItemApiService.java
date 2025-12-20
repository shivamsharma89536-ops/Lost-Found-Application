package com.example.lostandfoundapp.api; // Make sure this package name is correct

import com.example.lostandfoundapp.models.ClaimResponse;
import com.example.lostandfoundapp.models.Item;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ItemApiService {

    @GET("api/items/lost")
    Call<List<Item>> getLostItems();

    @GET("api/items/{id}")
    Call<Item> getLostItemById(@Path("id") String itemId);

    @Multipart
    @POST("api/items/lost")
    Call<Item> postLostItem(
            @Field("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("category") RequestBody category,
            @Part("date") RequestBody date,
            @Part("location") RequestBody location,
            @Part MultipartBody.Part image
    );

    @DELETE("api/items/{id}")
    Call<Void> deleteLostItem(@Path("id") String itemId);


    @GET("api/items/found")
    Call<List<Item>> getFoundItems();

    @Multipart
    @POST("api/items/found")
    Call<Item> postFoundItem(
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("category") RequestBody category,
            @Part("date") RequestBody date,
            @Part("location") RequestBody location,
            @Part MultipartBody.Part image
    );

    @POST("api/items/{id}/claim")
    Call<ClaimResponse> claimItem(@Path("id") String itemId);

    @GET("api/items/search")
    Call<List<Item>> searchItems(@QueryMap Map<String, String> options);


}