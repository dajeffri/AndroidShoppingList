package com.example.dajeffri.shoppinglist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

public class StoreItemJoinViewModel extends AndroidViewModel{
    private final DataRepository mDataRepository;


    public StoreItemJoinViewModel (Application application) {
        super(application);
        mDataRepository = new DataRepository(application);
    }

    public void insert(StoreItemJoin storeItemJoin) {
        mDataRepository.insert(storeItemJoin);
    }
}
