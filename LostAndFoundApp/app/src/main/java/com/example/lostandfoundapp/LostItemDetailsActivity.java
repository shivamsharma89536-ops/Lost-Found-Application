package com.example.lostandfoundapp;

import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.lostandfoundapp.api.ItemApiService;
import com.example.lostandfoundapp.api.RetrofitClient;
import com.example.lostandfoundapp.models.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LostItemDetailsActivity extends AppCompatActivity {

    private ImageView ivDetailImage;
    private TextView tvDetailTitle, tvDetailDescription, tvDetailCategory, tvDetailDate, tvDetailLocation;
    private Button btnEditItem, btnDeleteItem;
    private LinearLayout ownerControlsLayout;

    private ItemApiService itemApiService;
    private SessionManager sessionManager;
    private String itemId;
    private Item currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_item_details);

        // Get Item ID from Intent
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
        btnEditItem = findViewById(R.id.btnEditItem);
        btnDeleteItem = findViewById(R.id.btnDeleteItem);
        ownerControlsLayout = findViewById(R.id.ownerControlsLayout);

        itemApiService = RetrofitClient.getClient(this).create(ItemApiService.class);
        sessionManager = new SessionManager(this);

        loadItemDetails();

        btnDeleteItem.setOnClickListener(v -> deleteItem());

        btnEditItem.setOnClickListener(v -> {
            // TODO: Implement Edit Functionality
            Toast.makeText(this, "Edit not yet implemented", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadItemDetails() {
        itemApiService.getLostItemById(itemId).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentItem = response.body();
                    displayItemDetails();
                } else {
                    Toast.makeText(LostItemDetailsActivity.this, "Failed to load item details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Toast.makeText(LostItemDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayItemDetails() {
        tvDetailTitle.setText(currentItem.getTitle());
        tvDetailDescription.setText(currentItem.getDescription());
        tvDetailCategory.setText("Category: " + currentItem.getCategory());
        tvDetailDate.setText("Date Lost: " + currentItem.getDate());
        tvDetailLocation.setText("Location: " + currentItem.getLocation());

        Glide.with(this)
                .load(currentItem.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(ivDetailImage);


        String currentUserId = sessionManager.getUserId();

        if (currentItem.getPostedBy() == null) {
            Log.e("DetailsActivity", "PostedBy user object is NULL!");
        } else {
            Log.d("DetailsActivity", "Comparing IDs...");
            Log.d("DetailsActivity", "My User ID (from Session): " + currentUserId);
            Log.d("DetailsActivity", "Item Owner ID (from Item): " + currentItem.getPostedBy().getId());
        }


        if (currentUserId != null && currentItem.getPostedBy() != null &&
                currentUserId.equals(currentItem.getPostedBy().getId())) {

            Log.d("DetailsActivity", "SUCCESS: User is owner. Showing delete button.");
            ownerControlsLayout.setVisibility(View.VISIBLE);
        } else {
            Log.e("DetailsActivity", "FAILURE: User is NOT owner. Hiding delete button.");
        }
    }

    private void deleteItem() {
        if (currentItem == null) return;

        itemApiService.deleteLostItem(currentItem.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LostItemDetailsActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close details and go back to the list
                } else {
                    Toast.makeText(LostItemDetailsActivity.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LostItemDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}