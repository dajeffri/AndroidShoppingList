package com.example.dajeffri.shoppinglist;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

@Database(entities = {Store.class, Item.class, StoreItemJoin.class}, version = 1, exportSchema = false)
public abstract class ShoppingListRoomDatabase extends RoomDatabase{

    public abstract StoreDao storeDao();
    public abstract ItemDao itemDao();
    public abstract StoreItemJoinDao storeItemJoinDao();

    private static ShoppingListRoomDatabase INSTANCE;

    static ShoppingListRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ShoppingListRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ShoppingListRoomDatabase.class, "shopping_list_database")
//                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public void populateDB() { new PopulateDbAsync(INSTANCE).execute(); }

//    private static final RoomDatabase.Callback sRoomDatabaseCallback =
//            new RoomDatabase.Callback(){
//
//                @Override
//                public void onOpen (@NonNull SupportSQLiteDatabase db){
//                    super.onOpen(db);
//                    new PopulateDbAsync(INSTANCE).execute();
//                }
//            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final StoreDao mStoreDao;
        private final ItemDao mItemDao;
        private final StoreItemJoinDao mStoreItemJoinDao;

        PopulateDbAsync(ShoppingListRoomDatabase db) {
            mStoreDao = db.storeDao();
            mItemDao = db.itemDao();
            mStoreItemJoinDao = db.storeItemJoinDao();

        }

        @Override
        protected Void doInBackground(final Void... params) {
            mStoreItemJoinDao.deleteAll();
            mStoreDao.deleteAll();
            mItemDao.deleteAll();

            List<Store> stores = DataGenerator.generateStores();
            List<Item> items = DataGenerator.generateItems();
            List<StoreItemJoin> storeItemJoins = DataGenerator.generateStoreItemJoins();
            for (Store store : stores) {
                mStoreDao.insert(store);
            }
            for (Item item : items) {
                mItemDao.insert(item);
            }
            for (StoreItemJoin storeItemJoin : storeItemJoins) {
                mStoreItemJoinDao.insert(storeItemJoin);
            }

            return null;
        }
    }
}
