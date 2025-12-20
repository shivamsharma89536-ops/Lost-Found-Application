package com.Android.Android_App.Repository;

import com.Android.Android_App.Entity.Item;
import com.Android.Android_App.Entity.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    // This query matches: GET /api/items/lost and GET /api/items/found
    // It finds all items by their type (LOST or FOUND)
    List<Item> findByType(ItemType type);
}