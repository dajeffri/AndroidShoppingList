package com.example.dajeffri.shoppinglist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "item_table", indices = @Index(value = {"item"}))
public class Item {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "item")
    private final String mItemName;

    @ColumnInfo(name = "reoccurs")
    private final boolean mReoccurs;

    @ColumnInfo(name = "checked")
    private boolean mChecked;

    public Item(@NonNull String itemName, boolean reoccurs) {
        this.mItemName = itemName;
        this.mChecked = false;
        this.mReoccurs = reoccurs;
    }

    @NonNull
    public String getItemName() {
        return mItemName;
    }

    public boolean getReoccurs() {return mReoccurs; }

    public boolean getChecked() {return mChecked; }

    public void setChecked(boolean checked) {
        this.mChecked = checked;
    }

}