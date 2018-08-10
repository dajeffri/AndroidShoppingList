package com.example.dajeffri.shoppinglist;

import java.util.ArrayList;
import java.util.List;

class DataGenerator {

    public static List<Item> generateItems() {
        List<Item> items = new ArrayList<>();
        String itemName;
        boolean reoccurs;

        for (int i = 0; i<24; i++) {
            itemName = "Item" + i;
            reoccurs = i % 2 == 0;
            items.add(new Item(itemName, reoccurs));
        }
        return items;
    }

    public static List<Store> generateStores() {
        List<Store> stores = new ArrayList<>();
        String storeName;

        for (int i = 0; i<10; i++) {
            storeName = "Store" + i;
            stores.add(new Store(storeName));
        }
        return stores;
    }

    public static List<StoreItemJoin> generateStoreItemJoins() {
        List<StoreItemJoin> storeItemJoins = new ArrayList<>();
        String storeName;
        String itemName;

        for (int i = 0; i<100; i++) {
            storeName = "Store" + (i % 10);
            itemName = "Item" + (i % 24);
            storeItemJoins.add(new StoreItemJoin(storeName, itemName));
        }
        return storeItemJoins;
    }
}
