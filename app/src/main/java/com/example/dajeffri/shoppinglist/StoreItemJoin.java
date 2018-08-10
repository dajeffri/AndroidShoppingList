package com.example.dajeffri.shoppinglist;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "store_item_join",
        primaryKeys = {"mStoreName", "mItemName"},
        indices = {@Index(value = "mStoreName"),
                @Index(value = "mItemName")},
        foreignKeys = {
            @ForeignKey(entity = Store.class,
                        parentColumns = "store",
                        childColumns = "mStoreName",
                        onDelete = CASCADE),
            @ForeignKey(entity = Item.class,
                        parentColumns = "item",
                        childColumns = "mItemName",
                        onDelete = CASCADE)
        })
public class StoreItemJoin {
    @NonNull
    private final String mStoreName;
    @NonNull
    public final String mItemName;

    public StoreItemJoin(@NonNull String storeName, @NonNull String itemName) {
        this.mStoreName = storeName;
        this.mItemName = itemName;
    }

    @NonNull
    public String getStoreName() {
        return mStoreName;
    }
}
