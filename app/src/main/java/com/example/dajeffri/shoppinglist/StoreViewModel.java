package com.example.dajeffri.shoppinglist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class StoreViewModel extends AndroidViewModel {

    private final DataRepository mDataRepository;

    private final LiveData<List<Store>> mAllStores;

    public StoreViewModel (Application application) {
        super(application);
        mDataRepository = new DataRepository(application);
        mAllStores = mDataRepository.getAllStores();
    }

    LiveData<List<Store>> getAllStores() {
        return mAllStores;
    }

    public void insert(Store store) {
        mDataRepository.insert(store);
    }

    public void deleteItems() { mDataRepository.deleteItems(); }

    public void deleteStore(Store store) { mDataRepository.deleteStore(store.getStoreName()); }
}
