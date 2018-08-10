package com.example.dajeffri.shoppinglist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Switch;

import com.example.dajeffri.shoppinglist.R;

public class ItemEditActivity extends AppCompatActivity{

    private EditText mEditItemName;
    private EditText mEditItemStore;
    private Switch mEditReoccurs;
    private ItemViewModel mItemViewModel;
    private StoreViewModel mStoreViewModel;
    private StoreItemJoinViewModel mStoreItemJoinViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_edit_layout);

        mEditItemName = findViewById(R.id.edit_item_name);
        mEditItemStore = findViewById(R.id.edit_store_name);
        mEditReoccurs = findViewById(R.id.edit_reoccurs);
        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        mStoreViewModel = ViewModelProviders.of(this).get(StoreViewModel.class);
        mStoreItemJoinViewModel = ViewModelProviders.of(this).get(StoreItemJoinViewModel.class);
    }

    @Override
    protected void onPause(){
        super.onPause();

        if (!TextUtils.isEmpty(mEditItemName.getText()) && !TextUtils.isEmpty(mEditItemStore.getText())) {
            String storeName = mEditItemStore.getText().toString();
            String itemName = mEditItemName.getText().toString();
            Item item = new Item(itemName, mEditReoccurs.isChecked());
            mItemViewModel.insert(item);
            Store store = new Store(storeName);
            mStoreViewModel.insert(store);
            StoreItemJoin storeItemJoin = new StoreItemJoin(storeName, itemName);
            mStoreItemJoinViewModel.insert(storeItemJoin);
        }
    }
}
