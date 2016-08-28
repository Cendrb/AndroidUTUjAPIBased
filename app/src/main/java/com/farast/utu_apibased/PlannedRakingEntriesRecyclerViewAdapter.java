package com.farast.utu_apibased;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farast.utuapi.data.PlannedRakingEntry;

import java.util.List;

/**
 * Created by cendr_000 on 28.08.2016.
 */

public class PlannedRakingEntriesRecyclerViewAdapter extends RecyclerView.Adapter<PlannedRakingEntriesRecyclerViewAdapter.ViewHolder> {

    private List<PlannedRakingEntry> mData;
    private Context mContext;

    public PlannedRakingEntriesRecyclerViewAdapter(Context context, List<PlannedRakingEntry> data) {
        mData = data;
        mContext = context;
    }

    public void setData(List<PlannedRakingEntry> entries) {
        mData.clear();
        mData.addAll(entries);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raking_entry_line, parent, false);
        return new PlannedRakingEntriesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PlannedRakingEntry plannedRakingEntry = mData.get(position);
        holder.mNameView.setText(plannedRakingEntry.getClassMember().getFullName());
        if (plannedRakingEntry.isFinished()) {
            holder.mLayoutView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rekt));
            if (plannedRakingEntry.getGrade() != -1 && Bullshit.dataLoader.getCurrentUser() != null && plannedRakingEntry.getClassMember() == Bullshit.dataLoader.getCurrentUser().getClassMember())
                holder.mStateView.setText(String.valueOf(plannedRakingEntry.getGrade()));
            else
                holder.mStateView.setText(R.string.raking_rekt);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mLayoutView;
        public TextView mNameView;
        public TextView mStateView;

        public ViewHolder(View view) {
            super(view);
            mLayoutView = view.findViewById(R.id.raking_layout);
            mNameView = (TextView) view.findViewById(R.id.raking_name_text);
            mStateView = (TextView) view.findViewById(R.id.raking_state_text);
        }
    }
}
