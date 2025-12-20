package com.Android.Android_App.Service;

import com.Android.Android_App.Entity.Item;
import com.Android.Android_App.Entity.ItemType;
import com.Android.Android_App.Entity.User;
import com.Android.Android_App.Repository.ItemRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final StorageService storageService;

    public ItemService(ItemRepository itemRepository, StorageService storageService) {
        this.itemRepository = itemRepository;
        this.storageService = storageService;
    }

    public List<Item> getItemsByType(ItemType type) {
        return itemRepository.findByType(type);
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public Item createItem(String title, String desc, String category, String date, String location, MultipartFile image, User user, ItemType type) {
        // 1. Store the image
        String imageUrl = storageService.store(image);
        
        // TODO: You need to make this URL accessible
        // e.g., "http://10.0.2.2:8080/api/files/" + imageUrl
        // For now, we'll just store the filename
        
        // 2. Create the Item entity
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(desc);
        item.setCategory(category);
        item.setDate(LocalDate.parse(date)); // Convert string to date
        item.setLocation(location);
        item.setImageUrl(imageUrl); // Set the path/URL
        item.setPostedBy(user);
        item.setType(type);

        // 3. Save to database
        return itemRepository.save(item);
    }

    public void deleteItem(Long id, User currentUser) {
        Item item = getItemById(id);
        // Security check: Only the owner can delete
        if (!item.getPostedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this item");
        }
        // TODO: Also delete the file from storageService
        itemRepository.delete(item);
    }
    
    // This is the logic for Module 6: Search
    public List<Item> searchItems(Map<String, String> params) {
        Specification<Item> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.containsKey("type")) {
                predicates.add(cb.equal(root.get("type"), ItemType.valueOf(params.get("type"))));
            }
            if (params.containsKey("q")) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + params.get("q").toLowerCase() + "%"));
            }
            if (params.containsKey("category")) {
                predicates.add(cb.equal(root.get("category"), params.get("category")));
            }
            if (params.containsKey("date")) {
                predicates.add(cb.equal(root.get("date"), LocalDate.parse(params.get("date"))));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return itemRepository.findAll(spec);
    }
}