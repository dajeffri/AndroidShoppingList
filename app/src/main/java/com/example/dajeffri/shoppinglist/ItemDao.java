package com.example.dajeffri.shoppinglist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;


@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Item item);

    @Query("DELETE FROM item_table")
    void deleteAll();

    @Query("SELECT * FROM item_table WHERE item = :itemName")
    Item getItem(String itemName);

    @Query("UPDATE item_table SET checked = :checked WHERE item = :item")
    void setChecked(boolean checked, String item);

    @Query("DELETE FROM item_table WHERE checked = 1 AND reoccurs = 0")
    void deleteItems();

    @Query("UPDATE item_table SET checked = 0")
    void unCheck();

    @Query("DELETE FROM item_table WHERE item = :itemName")
    void deleteItem(String itemName);

    @Query("DELETE FROM item_table WHERE item NOT IN (SELECT mItemName FROM store_item_join)")
    void deleteLooseItems();
}
