package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SessionManager sessionManager = new SessionManager(SplashActivity.this);
            String token = sessionManager.fetchAuthToken();

            Intent intent;
            if (token != null) {
                // Token exists, go to Home (MainActivity)
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // No token, go to Login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish(); // Close the splash activity
        }, 2000);
    }
}