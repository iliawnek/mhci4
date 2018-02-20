package uk.ac.gla.shopping.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import uk.ac.gla.shopping.entity.ShoppingListItem;

@Dao
public interface ShoppingListItemDAO {
    @Query("SELECT * FROM ShoppingListItem")
    LiveData<List<ShoppingListItem>> getAllShoppingListItems();

    @Query("SELECT * FROM ShoppingListItem WHERE id = :id LIMIT 1")
    LiveData<List<ShoppingListItem>> findShoppingListItemById(long id);

    @Query("SELECT * FROM ShoppingListItem WHERE name LIKE :name LIMIT 1")
    LiveData<List<ShoppingListItem>> findShoppingListItemByName(String name);

    @Query("SELECT COUNT(*) FROM ShoppingListItem")
    int rowCount();

    @Insert
    void insertShoppingListItems(ShoppingListItem... ShoppingListItems);

    @Update
    void updateShoppingListItems(ShoppingListItem... ShoppingListItems);

    @Delete
    void deleteShoppingListItems(ShoppingListItem... ShoppingListItems);
}