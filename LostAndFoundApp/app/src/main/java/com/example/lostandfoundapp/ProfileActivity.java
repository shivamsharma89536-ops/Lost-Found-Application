package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.lostandfoundapp.api.AuthApiService;
import com.example.lostandfoundapp.api.RetrofitClient;
import com.example.lostandfoundapp.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ImageView ivProfile;
    private TextView tvProfileName, tvProfileEmail, tvProfilePhone;
    private Button btnEditProfile, btnLogout;

    private Button btnMyClaims; // <-- ADD THIS


    private Button btnMyMessages; // <-- Add btnMyMessages

    private AuthApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfile = findViewById(R.id.ivProfile);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);
        tvProfilePhone = findViewById(R.id.tvProfilePhone);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        btnMyClaims = findViewById(R.id.btnMyClaims); // <-- ADD THIS
        btnMyMessages = findViewById(R.id.btnMyMessages); // <-- ADD THIS

        apiService = RetrofitClient.getClient(this).create(AuthApiService.class);
        sessionManager = new SessionManager(this);

        loadUserProfile();

        btnLogout.setOnClickListener(v -> logoutUser());

        // TODO: Implement Edit Profile
        btnEditProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Not done", Toast.LENGTH_SHORT).show();
        });
        btnMyClaims.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, MyClaimsActivity.class));
        });
        btnMyMessages.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, ConversationListActivity.class));
        });
    }

    private void loadUserProfile() {
        apiService.getCurrentUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    tvProfileName.setText(user.getName());
                    tvProfileEmail.setText("Email: " + user.getEmail());
                    tvProfilePhone.setText("Phone: " + user.getPhone());

                    // Load profile image using Glide
                    Glide.with(ProfileActivity.this)
                            .load(user.getProfileImageUrl())
                            .placeholder(R.drawable.ic_launcher_background) // Default image
                            .error(R.drawable.ic_launcher_background) // Error image
                            .circleCrop()
                            .into(ivProfile);
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                    // If token is invalid, log out
                    if (response.code() == 401 || response.code() == 403) {
                        logoutUser();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        sessionManager.clearAuthToken();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}