package com.Android.Android_App.Controller;

import com.Android.Android_App.Entity.Item;
import com.Android.Android_App.Entity.ItemType;
import com.Android.Android_App.Entity.User;
import com.Android.Android_App.Service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // --- Module 2: GET All Lost Items ---
    // Matches: GET /api/items/lost
    @GetMapping("/lost")
    public ResponseEntity<List<Item>> getLostItems() {
        return ResponseEntity.ok(itemService.getItemsByType(ItemType.LOST));
    }

    // --- Module 3: GET All Found Items ---
    // Matches: GET /api/items/found
    @GetMapping("/found")
    public ResponseEntity<List<Item>> getFoundItems() {
        return ResponseEntity.ok(itemService.getItemsByType(ItemType.FOUND));
    }

    // --- Module 2 & 3: GET Single Item ---
    // Matches: GET /api/items/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    // --- Module 2: POST Lost Item ---
    // Matches: POST /api/items/lost
    @PostMapping("/lost")
    public ResponseEntity<Item> postLostItem(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("date") String date,
            @RequestParam("location") String location,
            @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal User currentUser
    ) {
        Item item = itemService.createItem(title, description, category, date, location, image, currentUser, ItemType.LOST);
        return ResponseEntity.ok(item);
    }

    // --- Module 3: POST Found Item ---
    // Matches: POST /api/items/found
    @PostMapping("/found")
    public ResponseEntity<Item> postFoundItem(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("date") String date,
            @RequestParam("location") String location,
            @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal User currentUser
    ) {
        Item item = itemService.createItem(title, description, category, date, location, image, currentUser, ItemType.FOUND);
        return ResponseEntity.ok(item);
    }

    // --- Module 2: DELETE Lost Item ---
    // Matches: DELETE /api/items/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        itemService.deleteItem(id, currentUser);
        return ResponseEntity.ok().build();
    }

    // --- Module 6: Search API ---
    // Matches: GET /api/items/search?type=...&q=...&category=...
    @GetMapping("/search")
    public ResponseEntity<List<Item>> searchItems(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(itemService.searchItems(params));
    }
}