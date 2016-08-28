package com.farast.utu_apibased.fragments.raking;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.UtuLineGenericViewHolder;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Event;
import com.farast.utuapi.data.PlannedRakingList;

import java.util.List;

/**
 * Created by cendr_000 on 27.08.2016.
 */
public class RakingsRecyclerViewAdapter extends RecyclerView.Adapter<UtuLineGenericViewHolder> {
    private final List<PlannedRakingList> mValues;
    private final OnListFragmentInteractionListener<PlannedRakingList> mListener;
    private final Context mContext;

    public RakingsRecyclerViewAdapter(OnListFragmentInteractionListener<PlannedRakingList> listener, Context context) {
        mValues = Bullshit.dataLoader.getPlannedRakingsListsList();
        mListener = listener;
        mContext = context;

        final Handler handler = new Handler(context.getMainLooper());

        Bullshit.dataLoader.getNotifier().setRakingsListener(new DataLoader.OnDataSetListener() {
            @Override
            public void onDataSetChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mValues.clear();
                        mValues.addAll(Bullshit.dataLoader.getPlannedRakingsListsList());
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public UtuLineGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.utu_line_generic, parent, false);
        return new UtuLineGenericViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UtuLineGenericViewHolder holder, int position) {
        final PlannedRakingList item = mValues.get(position);
        holder.mTitleView.setText(mContext.getString(R.string.planned_raking_list_title, item.getTitle(), item.getCurrentRound().getRoundNumber()));
        holder.mAvatarTextView.setText(item.getSubject().getName());
        holder.mLeftBottomView.setText(mContext.getString(R.string.planned_raking_list_description, item.getCurrentRound().getSignedUpRemainingCount()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
