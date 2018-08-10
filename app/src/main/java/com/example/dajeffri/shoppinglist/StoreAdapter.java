package com.example.dajeffri.shoppinglist;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dajeffri.shoppinglist.R;

import java.util.ArrayList;
import java.util.List;


public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private static final int FULL_LAYOUT_TYPE = 1;
    private ItemViewModel mItemViewModel;
    private StoreViewModel mStoreViewModel;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private List<Store> mStores;
    private int mLayoutType;
    private RecyclerView mStoreRecycler;
    private boolean mSingleStore;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView storeNameView;
        final RecyclerView mItemsRecyclerView;

        ViewHolder(View itemView) {
            super(itemView);

            storeNameView = itemView.findViewById(R.id.store_name);
            mItemsRecyclerView = itemView.findViewById(R.id.items_recycler);
        }
    }

    StoreAdapter(Context context) {
        mLayoutType = FULL_LAYOUT_TYPE;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void setLayoutType(int layoutType) {
        mLayoutType = layoutType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View storeView;
        mStoreRecycler = parent.findViewById(R.id.stores_recycler);
        storeView = mInflater.inflate(R.layout.store_layout, parent, false);
        return new ViewHolder(storeView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (mStores != null) {
            final Store current = mStores.get(position);
            holder.storeNameView.setText(current.getStoreName());
            holder.storeNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStores(current);
                    mLayoutType = FULL_LAYOUT_TYPE;
                    mSingleStore = true;
                    mStoreRecycler.setLayoutManager(new LinearLayoutManager(mContext));
                    notifyDataSetChanged();
                }
            });

            holder.storeNameView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mStoreViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(StoreViewModel.class);
                    mStoreViewModel.deleteStore(current);
                    return true;
                }
            });

            if (mLayoutType == FULL_LAYOUT_TYPE) {
                final ItemAdapter itemAdapter = new ItemAdapter(mContext);
                holder.mItemsRecyclerView.setAdapter(itemAdapter);
                holder.mItemsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

                mItemViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(ItemViewModel.class);
                mItemViewModel.getItemsFromStore(current.getStoreName()).observe((FragmentActivity) mContext, new Observer<List<Item>>() {
                    @Override
                    public void onChanged(@Nullable List<Item> items) {
                        itemAdapter.setItems(items);
                    }
                });
            }
        } else {
            holder.storeNameView.setText(mContext.getString(R.string.no_items_warning));
        }
    }

    void setStores(List<Store> stores) {
        mStores = stores;
        notifyDataSetChanged();
    }

    private void setStores(Store store) {
        mStores = new ArrayList<>();
        mStores.add(store);
    }

    public int getItemCount() {
        if (mStores != null)
            return mStores.size();
        else return 0;
    }

    public boolean getSingleStore() {return mSingleStore;}

    public void setSingleStore(boolean singleStore) {mSingleStore = singleStore;}

    public String getEmail() {
//        StringBuilder builder = new StringBuilder();
//        for (Store store: mStores) {
//            builder.append(store.getStoreName());
//            builder.append("\n");
//            List<Item> items = mItemViewModel.getDeadItemsFromStore(store.getStoreName());
//            for (Item item: items) {
//                builder.append("    ");
//                builder.append(item.getItemName());
//                builder.append("\n");
//            }
//            builder.append("\n");
//        }
//        return builder.toString();
        return mItemViewModel.getEmail(mStores);
    }

}
