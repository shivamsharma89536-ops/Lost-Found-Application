package com.example.lostandfoundapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lostandfoundapp.api.ItemApiService;
import com.example.lostandfoundapp.api.RetrofitClient;
import com.example.lostandfoundapp.models.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoundItemsFragment extends Fragment {

    private RecyclerView rvFoundItems;
    private FoundItemAdapter itemAdapter; // Use the FoundItemAdapter
    private List<Item> itemList;
    private FloatingActionButton fabPostFoundItem;
    private ProgressBar progressBar;
    private ItemApiService itemApiService;

    // --- NEW Module 6 UI ---
    private SearchView searchView;
    private Spinner spinnerCategory;
    private Button btnDateFilter;

    // --- NEW Module 6 Search State ---
    private String currentQuery = "";
    private String currentCategory = "";
    private String currentDate = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found_items, container, false);

        rvFoundItems = view.findViewById(R.id.rvFoundItems);
        fabPostFoundItem = view.findViewById(R.id.fabPostFoundItem);
        progressBar = view.findViewById(R.id.progressBar);

        // --- Init Module 6 UI ---
        searchView = view.findViewById(R.id.searchView);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        btnDateFilter = view.findViewById(R.id.btnDateFilter);

        itemList = new ArrayList<>();
        itemAdapter = new FoundItemAdapter(getContext(), itemList); // Use FoundItemAdapter
        rvFoundItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFoundItems.setAdapter(itemAdapter);

        itemApiService = RetrofitClient.getClient(getContext()).create(ItemApiService.class);

        fabPostFoundItem.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), PostFoundItemActivity.class));
        });

        // --- Setup Module 6 Listeners ---
        setupSearchAndFilters();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load initial data (no filters)
        loadItems(new HashMap<>());
    }

    private void setupSearchAndFilters() {
        // --- Search View Listener ---
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentQuery = query;
                performSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    currentQuery = "";
                    performSearch();
                }
                return true;
            }
        });

        // --- Category Spinner Setup ---
        String[] categories = {"All Categories", "Electronics", "Wallets", "Keys", "Books", "Clothing", "Other"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    currentCategory = ""; // "All Categories"
                } else {
                    currentCategory = parent.getItemAtPosition(position).toString();
                }
                performSearch();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // --- Date Picker Button ---
        btnDateFilter.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            currentDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
            btnDateFilter.setText(currentDate);
            performSearch();
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEUTRAL, "Clear", (dialog, which) -> {
            currentDate = "";
            btnDateFilter.setText("Date Found"); // <-- Text change
            performSearch();
        });
        datePickerDialog.show();
    }

    private void performSearch() {
        Map<String, String> filters = new HashMap<>();

        // --- KEY CHANGE: Set type to FOUND ---
        filters.put("type", "FOUND");

        if (!currentQuery.isEmpty()) {
            filters.put("q", currentQuery);
        }
        if (!currentCategory.isEmpty()) {
            filters.put("category", currentCategory);
        }
        if (!currentDate.isEmpty()) {
            filters.put("date", currentDate);
        }

        loadItems(filters);
    }

    private void loadItems(Map<String, String> filters) {
        progressBar.setVisibility(View.VISIBLE);
        rvFoundItems.setVisibility(View.GONE);

        Call<List<Item>> apiCall;

        if (filters.isEmpty() || (filters.size() == 1 && filters.containsKey("type") && filters.get("type").equals("FOUND"))) {
            apiCall = itemApiService.getFoundItems(); // Call getFoundItems
        } else {
            apiCall = itemApiService.searchItems(filters); // Call search
        }

        apiCall.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                progressBar.setVisibility(View.GONE);
                rvFoundItems.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    itemList.clear();
                    itemList.addAll(response.body());
                    itemAdapter.setItems(itemList);
                } else {
                    Toast.makeText(getContext(), "Failed to load found items", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}