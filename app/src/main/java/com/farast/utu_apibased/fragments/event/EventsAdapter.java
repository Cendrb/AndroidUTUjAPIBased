package com.farast.utu_apibased.fragments.event;

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

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Event} and makes a call to the
 * specified {@link OnListFragmentInteractionListener<Event> }.
 */
public class EventsAdapter extends RecyclerView.Adapter<UtuLineGenericViewHolder> {

    private final List<Event> mValues;
    private final OnListFragmentInteractionListener<Event> mListener;
    private DataLoader.OnDataSetListener mDataSetListener;

    public EventsAdapter(OnListFragmentInteractionListener<Event> listener, Context context) {
        mValues = Bullshit.dataLoader.getEventsList();
        mListener = listener;

        final Handler handler = new Handler(context.getMainLooper());

        mDataSetListener = new DataLoader.OnDataSetListener() {
            @Override
            public void onDataSetChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mValues.clear();
                        mValues.addAll(Bullshit.dataLoader.getEventsList());
                        notifyDataSetChanged();
                    }
                });
            }
        };
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        Bullshit.dataLoader.getNotifier().addListener(DataLoader.EventType.EVENTS, mDataSetListener);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        Bullshit.dataLoader.getNotifier().removeListener(DataLoader.EventType.EVENTS, mDataSetListener);
    }

    @Override
    public UtuLineGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.utu_line_generic, parent, false);
        return new UtuLineGenericViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UtuLineGenericViewHolder holder, int position) {
        final Event item = mValues.get(position);
        holder.mTitleView.setText(item.getTitle());
        holder.mAvatarParent.setVisibility(View.GONE);
        holder.mLeftBottomView.setText(Bullshit.prettyDate(item.getStart()));

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
