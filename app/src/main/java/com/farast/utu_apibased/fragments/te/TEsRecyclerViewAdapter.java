package com.farast.utu_apibased.fragments.te;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.UtuLineGenericViewHolder;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utuapi.data.Article;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.TEItem;
import com.farast.utuapi.util.DateUtil;

import java.util.List;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class TEsRecyclerViewAdapter extends RecyclerView.Adapter<UtuLineGenericViewHolder> {
    private final List<TEItem> mValues;
    private final OnListFragmentInteractionListener<TEItem> mListener;

    public TEsRecyclerViewAdapter(OnListFragmentInteractionListener<TEItem> listener, Context context) {
        mValues = Bullshit.dataLoader.getTEsList();
        mListener = listener;

        final Handler handler = new Handler(context.getMainLooper());

        DataLoader.OnDataSetListener dataSetListener = new DataLoader.OnDataSetListener() {
            @Override
            public void onDataSetChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mValues.clear();
                        mValues.addAll(Bullshit.dataLoader.getTEsList());
                        notifyDataSetChanged();
                    }
                });
            }
        };

        Bullshit.dataLoader.getNotifier().setExamsListener(dataSetListener);
        Bullshit.dataLoader.getNotifier().setTasksListener(dataSetListener);
    }

    @Override
    public UtuLineGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.utu_line_generic, parent, false);
        return new UtuLineGenericViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UtuLineGenericViewHolder holder, int position) {
        final TEItem item = mValues.get(position);
        holder.mTitleView.setText(item.getTitle());
        holder.mAvatarTextView.setText(item.getSubject().getName());
        holder.mLeftBottomView.setText(Bullshit.formatDate(item.getDate()));

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
