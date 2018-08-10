package com.example.dajeffri.shoppinglist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dajeffri.shoppinglist.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemNameView;

        ViewHolder(View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.item_name);
        }
    }

    private final LayoutInflater mInflater;
    private List<Item> mItems;
    private final ItemViewModel mItemViewModel;
    private final Context mContext;

    ItemAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItemViewModel = ViewModelProviders.of((FragmentActivity) context).get(ItemViewModel.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (mItems != null) {
            final Item current = mItems.get(holder.getAdapterPosition());
            holder.itemNameView.setText(current.getItemName());
            holder.itemNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemViewModel.setChecked(current);
                    TextView tv = v.findViewById(R.id.item_name);

                    setStrikeThrough(tv, mItems.get(holder.getAdapterPosition()));
                }
            });
            holder.itemNameView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemViewModel.deleteItem(current);
                    return true;
                }
            });
            setStrikeThrough(holder.itemNameView, current);
        } else {
            holder.itemNameView.setText(mContext.getString(R.string.no_items_warning));
        }
    }

    void setItems(List<Item> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (mItems != null)
            return mItems.size();
        else return 0;
    }

    private void setStrikeThrough(TextView tv, Item item) {
        if(item.getChecked()) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            tv.setPaintFlags(tv.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

    }
}
