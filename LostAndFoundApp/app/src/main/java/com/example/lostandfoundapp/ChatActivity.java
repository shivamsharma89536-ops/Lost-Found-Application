package com.example.lostandfoundapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lostandfoundapp.api.ChatApiService;
import com.example.lostandfoundapp.api.RetrofitClient;
import com.example.lostandfoundapp.models.Message;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    // IMPORTANT: Use "ws://" and the 10.0.2.2 emulator IP
    private static final String WS_URL = "ws://10.0.2.2:8080/ws/chat/websocket";

    private RecyclerView rvMessages;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText etMessageInput;
    private ImageButton btnSendMessage;
    private MaterialToolbar toolbar;

    private String conversationId;
    private String currentUserId;
    private String otherUserName;

    private ChatApiService chatApiService;
    private SessionManager sessionManager;
    private Gson gson;

    // --- New STOMP variables ---
    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        conversationId = getIntent().getStringExtra("CONVERSATION_ID");
        otherUserName = getIntent().getStringExtra("OTHER_USER_NAME");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(otherUserName != null ? "Chat with " + otherUserName : "Chat");
        toolbar.setNavigationOnClickListener(v -> finish());

        rvMessages = findViewById(R.id.rvMessages);
        etMessageInput = findViewById(R.id.etMessageInput);
        btnSendMessage = findViewById(R.id.btnSendMessage);

        sessionManager = new SessionManager(this);
        currentUserId = sessionManager.getUserId();
        chatApiService = RetrofitClient.getClient(this).create(ChatApiService.class);
        gson = new Gson();
        compositeDisposable = new CompositeDisposable();

        // Init RecyclerView
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList, currentUserId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(messageAdapter);

        btnSendMessage.setOnClickListener(v -> sendMessage());

        loadMessageHistory();
        connectStomp();
    }

    private void loadMessageHistory() {
        chatApiService.getMessages(conversationId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageAdapter.setMessages(response.body());
                    rvMessages.scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(ChatActivity.this, "Failed to load history", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connectStomp() {
        // Create the STOMP client
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WS_URL);

        // Add authentication headers (optional but good practice)
        // Note: Simple STOMP setup might not support headers this way.
        // Your backend's WebSocketConfig must be set up to handle auth.
        // For now, we assume auth is handled or open for simplicity.

        // Subscribe to lifecycle events
        Disposable lifecycleDisposable = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.d(TAG, "STOMP connection opened");
                            // Once connected, subscribe to the topic
                            subscribeToTopic();
                            break;
                        case ERROR:
                            Log.e(TAG, "STOMP connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.d(TAG, "STOMP connection closed");
                            break;
                    }
                });

        compositeDisposable.add(lifecycleDisposable);

        // Connect
        mStompClient.connect();
    }

    private void subscribeToTopic() {
        // Subscribe to the specific conversation topic
        String topicPath = "/topic/conversation/" + conversationId;

        Disposable topicDisposable = mStompClient.topic(topicPath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stompMessage -> {
                    Log.d(TAG, "Received STOMP message: " + stompMessage.getPayload());
                    try {
                        // Deserialize the message payload (which is a Message object)
                        Message message = gson.fromJson(stompMessage.getPayload(), Message.class);

                        // Add message to adapter
                        messageAdapter.addMessage(message);
                        rvMessages.scrollToPosition(messageList.size() - 1);

                    } catch (JsonSyntaxException e) {
                        Log.e(TAG, "Error parsing STOMP message", e);
                    }
                }, throwable -> {
                    Log.e(TAG, "Error on STOMP topic subscription", throwable);
                });

        compositeDisposable.add(topicDisposable);
    }

    private void sendMessage() {
        String messageContent = etMessageInput.getText().toString().trim();
        if (messageContent.isEmpty()) {
            return;
        }

        // Create the DTO that the backend expects
        // This must match ChatMessageDto.java on the server
        ChatMessageDto chatMessage = new ChatMessageDto();
        chatMessage.setContent(messageContent);
        chatMessage.setConversationId(conversationId);
        chatMessage.setSenderId(currentUserId);

        // Convert the DTO to a JSON string
        String jsonMessage = gson.toJson(chatMessage);

        // Send the message to the backend's @MessageMapping("/chat")
        Disposable sendDisposable = mStompClient.send("/app/chat", jsonMessage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d(TAG, "STOMP message sent");
                    etMessageInput.setText("");
                }, throwable -> {
                    Log.e(TAG, "Error sending STOMP message", throwable);
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
                });

        compositeDisposable.add(sendDisposable);

        // Note: We no longer do the optimistic update here.
        // We let the server broadcast the message back to us via the subscription.
        // This ensures the message was *actually* saved.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disconnect STOMP and clear all subscriptions
        if (mStompClient != null) {
            mStompClient.disconnect();
        }
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    // --- This is the DTO class. For simplicity, we define it here as a nested class ---
    // It must match ChatMessageDto.java on your server
    private static class ChatMessageDto {
        private String content;
        private String conversationId;
        private String senderId;

        public void setContent(String content) { this.content = content; }
        public void setConversationId(String conversationId) { this.conversationId = conversationId; }
        public void setSenderId(String senderId) { this.senderId = senderId; }
    }
}