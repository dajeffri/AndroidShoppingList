package com.example.dajeffri.shoppinglist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    private final DataRepository mDataRepository;

    public ItemViewModel (Application application) {
        super(application);
        mDataRepository = new DataRepository(application);
    }


    LiveData<List<Item>> getItemsFromStore(String store) {
        return mDataRepository.getItemsFromStore(store);
    }
//
//    List<Item> getDeadItemsFromStore(String store) {
//        return mDataRepository.getDeadItemsFromStore(store);
//    }

    String getEmail(List<Store> stores) {
        return mDataRepository.getEmail(stores);
    }

    public void insert(Item item) {
        mDataRepository.insert(item);
    }

    public void setChecked(Item item) {
        mDataRepository.setChecked(!item.getChecked(), item.getItemName());
    }

    public void deleteItem(Item item) {
        mDataRepository.deleteItem(item.getItemName());
    }
}
