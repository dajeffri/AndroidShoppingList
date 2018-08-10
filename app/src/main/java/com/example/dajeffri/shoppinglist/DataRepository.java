package com.example.dajeffri.shoppinglist;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import java.util.List;

class DataRepository {

    private final StoreDao mStoreDao;
    private final ItemDao mItemDao;
    private final StoreItemJoinDao mStoreItemJoinDao;
    private final LiveData<List<Store>> mAllStores;
    private final ShoppingListRoomDatabase mDb;

    DataRepository(Application application) {
        mDb = ShoppingListRoomDatabase.getDatabase(application);
        mStoreDao = mDb.storeDao();
        mItemDao = mDb.itemDao();
        mStoreItemJoinDao = mDb.storeItemJoinDao();
        mAllStores = mStoreDao.getAllStores();
    }

    LiveData<List<Store>> getAllStores() {
        return mAllStores;
    }

    LiveData<List<Item>> getItemsFromStore(String store) {
        return mStoreItemJoinDao.getItemsForStore(store);
    }

    Item getItem(String itemName) {
        return mItemDao.getItem(itemName);
    }

    Store getStore(String storeName) { return mStoreDao.getStore(storeName); }

    StoreItemJoin getStoreItemJoin(String itemName) {
        return mStoreItemJoinDao.getStoreItemJoin(itemName);
    }

    public void populateDB() { mDb.populateDB(); }

    public void insert(Store store) {
        new insertStoreAsyncTask(mStoreDao).execute(store);
    }

    public void insert(Item item) {
        new insertItemAsyncTask(mItemDao).execute(item);
    }

    public void insert(StoreItemJoin storeItemJoin) { new insertStoreItemJoinAsyncTask(mStoreItemJoinDao).execute(storeItemJoin);}

    public void setChecked(boolean checked, String item) { new setCheckedAsyncTask(checked, item, mItemDao).execute();}

    public void deleteItems() { new deleteItemsAsyncTask(mItemDao).execute();}

    public void deleteItem(String itemName) { new deleteItemAsyncTask(itemName, mItemDao).execute();}

    public void deleteStore(String storeName) { new deleteStoreAsyncTask(storeName, mStoreDao).execute();}

    public String getEmail(List<Store> stores) {
        AsyncTask<String, Void, String> task =  new getEmailAsyncTask(mStoreItemJoinDao, stores).execute();
        try {
            return task.get();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Item> getDeadItemsFromStore(String store) {
        return mStoreItemJoinDao.getDeadItemsForStore(store);
    }

    private static class insertStoreAsyncTask extends AsyncTask<Store, Void, Void> {

        private final StoreDao mAsyncTaskDao;

        insertStoreAsyncTask(StoreDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Store... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }

    private static class insertItemAsyncTask extends AsyncTask<Item, Void, Void> {

        private final ItemDao mAsyncTaskDao;

        insertItemAsyncTask(ItemDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Item... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }

    private static class insertStoreItemJoinAsyncTask extends AsyncTask<StoreItemJoin, Void, Void> {

        private final StoreItemJoinDao mAsyncTaskDao;

        insertStoreItemJoinAsyncTask(StoreItemJoinDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final StoreItemJoin... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }

    }

    private static class setCheckedAsyncTask extends AsyncTask<Item, Void, Void> {
        private final ItemDao mAsyncTaskDao;
        private final boolean mChecked;
        private final String mItem;

        setCheckedAsyncTask(boolean checked, String item, ItemDao itemDao) {
            mAsyncTaskDao = itemDao;
            mChecked = checked;
            mItem = item;
        }
        @Override
        protected Void doInBackground(final Item... params) {
            mAsyncTaskDao.setChecked(mChecked, mItem);
            return null;
        }
    }

    private static class deleteItemsAsyncTask extends AsyncTask<Item, Void, Void> {
        private final ItemDao mAsyncTaskDao;

        deleteItemsAsyncTask(ItemDao itemDao) {
            mAsyncTaskDao = itemDao;
        }
        @Override
        protected Void doInBackground(final Item... params) {

            mAsyncTaskDao.deleteItems();
            mAsyncTaskDao.unCheck();
            mAsyncTaskDao.deleteLooseItems();
            return null;
        }
    }

    private static class deleteItemAsyncTask extends AsyncTask<String, Void, Void> {
        private final ItemDao mAsyncTaskDao;
        private final String mItemName;

        deleteItemAsyncTask(String itemName, ItemDao itemDao) {
            mAsyncTaskDao = itemDao;
            mItemName = itemName;
        }
        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.deleteItem(mItemName);
            return null;
        }
    }

    private static class deleteStoreAsyncTask extends AsyncTask<String, Void, Void> {
        private final StoreDao mAsyncTaskDao;
        private final String mStoreName;

        deleteStoreAsyncTask(String storeName, StoreDao storeDao) {
            mAsyncTaskDao = storeDao;
            mStoreName = storeName;
        }
        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.deleteStore(mStoreName);
            return null;
        }
    }

    private static class getEmailAsyncTask extends AsyncTask<String, Void, String> {
        private final StoreItemJoinDao mStoreItemJoinDao;
        private final List<Store> mStores;
        private StringBuilder mBuilder;

        getEmailAsyncTask(StoreItemJoinDao storeItemJoinDao, List<Store> stores) {
            mStores = stores;
            mStoreItemJoinDao = storeItemJoinDao;
        }

        @Override
        protected String doInBackground(String... strings) {
            mBuilder = new StringBuilder();
            for (Store store: mStores) {
                mBuilder.append(store.getStoreName());
                mBuilder.append("\n");
                List<Item> items = mStoreItemJoinDao.getDeadItemsForStore(store.getStoreName());
                for (Item item: items) {
                    mBuilder.append("    ");
                    mBuilder.append(item.getItemName());
                    mBuilder.append("\n");
                    if (isCancelled()) break;
            }
                mBuilder.append("\n");
                if (isCancelled()) break;
            }
            return mBuilder.toString();
        }
    }
}
