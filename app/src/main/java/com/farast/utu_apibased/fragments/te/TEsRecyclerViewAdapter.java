package com.farast.utu_apibased.fragments.te;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farast.utu_apibased.R;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utuapi.data.TEItem;
import com.farast.utuapi.util.DateUtil;

import java.util.List;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class TEsRecyclerViewAdapter extends RecyclerView.Adapter<TEsRecyclerViewAdapter.ViewHolder> {
    private final List<TEItem> mValues;
    private final OnListFragmentInteractionListener<TEItem> mListener;

    public TEsRecyclerViewAdapter(List<TEItem> items, OnListFragmentInteractionListener<TEItem> listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public TEsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        return new TEsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TEsRecyclerViewAdapter.ViewHolder holder, int position) {
        TEItem item = mValues.get(position);
        holder.mItem = item;
        holder.mTitleView.setText(item.getTitle());
        holder.mDateView.setText(DateUtil.CZ_SHORT_DATE_FORMAT.format(item.getDate()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mDateView;
        public TEItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.event_title);
            mDateView = (TextView) view.findViewById(R.id.event_date);
        }
    }
}
