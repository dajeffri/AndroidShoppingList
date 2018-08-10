package com.example.dajeffri.shoppinglist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "store_table")
public class Store {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "store")
    private final String mStoreName;

    public Store(@NonNull String storeName) {
        this.mStoreName = storeName;
    }

    @NonNull
    public String getStoreName() {
        return mStoreName;
    }
}
