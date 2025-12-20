package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.lostandfoundapp.api.ItemApiService;
import com.example.lostandfoundapp.api.RetrofitClient;
import com.example.lostandfoundapp.models.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.lostandfoundapp.models.ClaimResponse;

public class FoundItemDetailsActivity extends AppCompatActivity {

    private ImageView ivDetailImage;
    private TextView tvDetailTitle, tvDetailDescription, tvDetailCategory, tvDetailDate, tvDetailLocation;
    private Button btnClaimItem;

    private ItemApiService itemApiService;
    private SessionManager sessionManager;
    private String itemId;
    private Item currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_item_details);

        itemId = getIntent().getStringExtra("ITEM_ID");
        if (itemId == null) {
            Toast.makeText(this, "Error: Item ID missing", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        btnClaimItem = findViewById(R.id.btnClaimItem);

        itemApiService = RetrofitClient.getClient(this).create(ItemApiService.class);
        sessionManager = new SessionManager(this);

        loadItemDetails();

        btnClaimItem.setOnClickListener(v -> showClaimConfirmationDialog());
    }

    private void loadItemDetails() {
        // We reuse the GET /api/items/{id} endpoint from Module 2
        itemApiService.getLostItemById(itemId).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentItem = response.body();
                    displayItemDetails();
                } else {
                    Toast.makeText(FoundItemDetailsActivity.this, "Failed to load item details", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText(FoundItemDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayItemDetails() {
        tvDetailTitle.setText(currentItem.getTitle());
        tvDetailDescription.setText(currentItem.getDescription());
        tvDetailCategory.setText("Category: " + currentItem.getCategory());
        tvDetailDate.setText("Date Found: " + currentItem.getDate()); // Changed text
        tvDetailLocation.setText("Location: " + currentItem.getLocation());

        Glide.with(this)
                .load(currentItem.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(ivDetailImage);

        // Show "Claim" button ONLY if the current user is NOT the one who posted it
        String currentUserId = sessionManager.getUserId();
        if (currentUserId != null && !currentUserId.equals(currentItem.getPostedBy())) {
            btnClaimItem.setVisibility(View.VISIBLE);
        }
    }

    private void showClaimConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Claim Item")
                .setMessage("Are you sure you want to claim this item? This will start a chat with the finder.")
                .setPositiveButton("Yes, Claim", (dialog, which) -> {
                    // Call the API to create the claim
                    claimItem();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void claimItem() {
        if (itemId == null) return;

        itemApiService.claimItem(itemId).enqueue(new Callback<ClaimResponse>() {
            @Override
            public void onResponse(Call<ClaimResponse> call, Response<ClaimResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(FoundItemDetailsActivity.this, "Claim request sent! Opening chat...", Toast.LENGTH_LONG).show();

                    String conversationId = response.body().getConversationId();


                    Intent intent = new Intent(FoundItemDetailsActivity.this, ChatActivity.class);
                    intent.putExtra("CONVERSATION_ID", conversationId);


                    startActivity(intent);
                    finish(); // Go back to the list for now

                } else {
                    Toast.makeText(FoundItemDetailsActivity.this, "Failed to send claim", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClaimResponse> call, Throwable t) {
                Toast.makeText(FoundItemDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}