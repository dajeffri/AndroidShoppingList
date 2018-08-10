package com.example.dajeffri.shoppinglist;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface StoreItemJoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StoreItemJoin storeItemJoin);

    @Query("DELETE FROM store_item_join")
    void deleteAll();

    @Query("SELECT item, reoccurs, checked FROM item_table INNER JOIN store_item_join ON " +
            "item_table.item=store_item_join.mItemName WHERE " +
            "store_item_join.mStoreName=:storeName ORDER BY item COLLATE NOCASE ASC")
    LiveData<List<Item>> getItemsForStore(final String storeName);

    @Query("SELECT item, reoccurs, checked FROM item_table INNER JOIN store_item_join ON " +
            "item_table.item=store_item_join.mItemName WHERE " +
            "store_item_join.mStoreName=:storeName ORDER BY item COLLATE NOCASE ASC")
    List<Item> getDeadItemsForStore(final String storeName);

    @Query("SELECT * FROM store_item_join WHERE mItemName = :itemName LIMIT 1")
    StoreItemJoin getStoreItemJoin(final String itemName);
}
