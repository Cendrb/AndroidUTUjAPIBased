package com.farast.utu_apibased.custom_views.utu_clickable_item_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.farast.utu_apibased.R;
import com.farast.utuapi.data.interfaces.OnelineRepresentable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr on 09/02/2017.
 */

public class UtuItemsClickableAdapter<T extends OnelineRepresentable> extends RecyclerView.Adapter<UtuItemsClickableAdapter.ViewHolder> {

    private List<T> mItemsList;
    private OnItemSelectedListener<T> mItemSelectedListener;

    public UtuItemsClickableAdapter(List<T> items) {
        mItemsList = items;
    }

    public void setItems(List<T> items) {
        mItemsList.clear();
        mItemsList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_spinner_item, parent, false);
        return new UtuItemsClickableAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final T currentlyRenderedItem = mItemsList.get(position);
        holder.mView.setText(currentlyRenderedItem.getOnelineRepresentation());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemSelectedListener != null) {
                    mItemSelectedListener.onItemSelected(currentlyRenderedItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public void setItemSelectedListener(OnItemSelectedListener<T> itemSelectedListener) {
        this.mItemSelectedListener = itemSelectedListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mView;

        public ViewHolder(View view) {
            super(view);
            mView = (TextView) view.findViewById(R.id.text1);
        }
    }

    public interface OnItemSelectedListener<T> {
        void onItemSelected(T item);
    }
}
