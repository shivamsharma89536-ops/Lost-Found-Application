package com.example.lostandfoundapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lostandfoundapp.api.ClaimApiService;
import com.example.lostandfoundapp.api.RetrofitClient;
import com.example.lostandfoundapp.models.Claim;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyClaimsActivity extends AppCompatActivity {

    private RecyclerView rvClaims;
    private MyClaimsAdapter claimsAdapter;
    private List<Claim> claimList;
    private ClaimApiService claimApiService;
    private ProgressBar progressBar;
    private TextView tvNoClaims;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_claims);

        rvClaims = findViewById(R.id.rvClaims);
        progressBar = findViewById(R.id.progressBar);
        tvNoClaims = findViewById(R.id.tvNoClaims);
        toolbar = findViewById(R.id.toolbar);

        // Set up Toolbar back button
        toolbar.setNavigationOnClickListener(v -> finish());

        // Init RecyclerView
        claimList = new ArrayList<>();
        claimsAdapter = new MyClaimsAdapter(this, claimList);
        rvClaims.setLayoutManager(new LinearLayoutManager(this));
        rvClaims.setAdapter(claimsAdapter);

        // Init API Service
        claimApiService = RetrofitClient.getClient(this).create(ClaimApiService.class);

        loadClaims();
    }

    private void loadClaims() {
        progressBar.setVisibility(View.VISIBLE);
        rvClaims.setVisibility(View.GONE);
        tvNoClaims.setVisibility(View.GONE);

        claimApiService.getMyClaims().enqueue(new Callback<List<Claim>>() {
            @Override
            public void onResponse(Call<List<Claim>> call, Response<List<Claim>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isEmpty()) {
                        tvNoClaims.setVisibility(View.VISIBLE);
                    } else {
                        rvClaims.setVisibility(View.VISIBLE);
                        claimsAdapter.setClaims(response.body());
                    }
                } else {
                    Toast.makeText(MyClaimsActivity.this, "Failed to load claims", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Claim>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MyClaimsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}