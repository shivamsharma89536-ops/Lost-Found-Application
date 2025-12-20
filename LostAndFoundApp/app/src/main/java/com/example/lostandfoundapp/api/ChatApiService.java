package com.example.lostandfoundapp.api;

import com.example.lostandfoundapp.models.Conversation;
import com.example.lostandfoundapp.models.Message;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChatApiService {


    @GET("api/conversations")
    Call<List<Conversation>> getConversations();

    @GET("api/conversations/{id}/messages")
    Call<List<Message>> getMessages(@Path("id") String conversationId);


}