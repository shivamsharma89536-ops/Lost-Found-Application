package com.example.lostandfoundapp;

import com.example.lostandfoundapp.api.ItemApiService;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lostandfoundapp.api.RetrofitClient;
import com.example.lostandfoundapp.models.Item;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLostItemActivity extends AppCompatActivity {

    private ImageView ivItemPreview;
    private Button btnChooseImage, btnSubmitLostItem;
    private EditText etItemName, etDescription, etDateLost, etLocation;
    private AutoCompleteTextView actvCategory;

    private ItemApiService itemApiService;
    private Uri selectedImageUri;

    // Activity Result Launcher for picking an image
    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    ivItemPreview.setImageURI(selectedImageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_lost_item);

        ivItemPreview = findViewById(R.id.ivItemPreview);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSubmitLostItem = findViewById(R.id.btnSubmitLostItem);
        etItemName = findViewById(R.id.etItemName);
        etDescription = findViewById(R.id.etDescription);
        actvCategory = findViewById(R.id.actvCategory);
        etDateLost = findViewById(R.id.etDateLost);
        etLocation = findViewById(R.id.etLocation);

        itemApiService = RetrofitClient.getClient(this).create(ItemApiService.class);

        // Setup Category Dropdown
        String[] categories = {"Electronics", "Wallets", "Keys", "Books", "Clothing", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        actvCategory.setAdapter(adapter);

        btnChooseImage.setOnClickListener(v -> {
            // Launch the image picker
            pickImageLauncher.launch("image/*");
        });

        btnSubmitLostItem.setOnClickListener(v -> submitLostItem());
    }

    private void submitLostItem() {
        String title = etItemName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = actvCategory.getText().toString().trim();
        String date = etDateLost.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || category.isEmpty() || date.isEmpty() || location.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields and choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        File imageFile = createFileFromUri(this, selectedImageUri);
        if (imageFile == null) {
            Toast.makeText(this, "Failed to prepare image for upload", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create RequestBody for all parts
        RequestBody titlePart = RequestBody.create(title, MediaType.parse("text/plain"));
        RequestBody descPart = RequestBody.create(description, MediaType.parse("text/plain"));
        RequestBody categoryPart = RequestBody.create(category, MediaType.parse("text/plain"));
        RequestBody datePart = RequestBody.create(date, MediaType.parse("text/plain"));
        RequestBody locationPart = RequestBody.create(location, MediaType.parse("text/plain"));

        RequestBody imageFilePart = RequestBody.create(imageFile, MediaType.parse(getContentResolver().getType(selectedImageUri)));
        MultipartBody.Part imageMultipart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageFilePart);

        // Make API Call
        itemApiService.postLostItem(titlePart, descPart, categoryPart, datePart, locationPart, imageMultipart)
                .enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PostLostItemActivity.this, "Item posted successfully!", Toast.LENGTH_SHORT).show();
                            finish(); // Close activity and return to list
                        } else {
                            Toast.makeText(PostLostItemActivity.this, "Failed to post item", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        Toast.makeText(PostLostItemActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Helper method to create a temporary file from a Uri
    private File createFileFromUri(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            String[] projection = { MediaStore.Images.Media.DISPLAY_NAME };
            android.database.Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            String fileName = "temp_image.jpg";
            if (cursor != null) {
                cursor.moveToFirst();
                int nameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                fileName = cursor.getString(nameIndex);
                cursor.close();
            }

            File tempFile = new File(context.getCacheDir(), fileName);
            FileOutputStream fos = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            Log.e("FileUtil", "Error creating file from Uri", e);
            return null;
        }
    }
}