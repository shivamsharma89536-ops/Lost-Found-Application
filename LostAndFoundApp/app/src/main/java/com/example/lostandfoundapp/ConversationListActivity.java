package com.example.lostandfoundapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lostandfoundapp.api.ChatApiService;
import com.example.lostandfoundapp.api.RetrofitClient;
import com.example.lostandfoundapp.models.Conversation;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationListActivity extends AppCompatActivity {

    private RecyclerView rvConversations;
    private ConversationAdapter adapter;
    private List<Conversation> conversationList;
    private ChatApiService chatApiService;
    private SessionManager sessionManager;
    private ProgressBar progressBar;
    private TextView tvNoConversations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        rvConversations = findViewById(R.id.rvConversations);
        progressBar = findViewById(R.id.progressBar);
        tvNoConversations = findViewById(R.id.tvNoConversations);

        sessionManager = new SessionManager(this);
        chatApiService = RetrofitClient.getClient(this).create(ChatApiService.class);

        // Init RecyclerView
        conversationList = new ArrayList<>();
        adapter = new ConversationAdapter(this, conversationList, sessionManager.getUserId());
        rvConversations.setLayoutManager(new LinearLayoutManager(this));
        rvConversations.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadConversations();
    }

    private void loadConversations() {
        progressBar.setVisibility(View.VISIBLE);
        rvConversations.setVisibility(View.GONE);
        tvNoConversations.setVisibility(View.GONE);

        chatApiService.getConversations().enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isEmpty()) {
                        tvNoConversations.setVisibility(View.VISIBLE);
                    } else {
                        rvConversations.setVisibility(View.VISIBLE);
                        adapter.setConversations(response.body());
                    }
                } else {
                    Toast.makeText(ConversationListActivity.this, "Failed to load conversations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ConversationListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}