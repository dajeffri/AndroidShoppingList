package com.example.dajeffri.shoppinglist;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StoreDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Store store);

    @Query("DELETE FROM store_table")
    void deleteAll();

    @Query("SELECT * FROM store_table ORDER BY store COLLATE NOCASE ASC")
    LiveData<List<Store>> getAllStores();

    @Query("DELETE FROM store_table WHERE store = :storeName")
    void deleteStore(String storeName);

    @Query("SELECT * FROM store_table WHERE store = :storeName")
    Store getStore(String storeName);

}
