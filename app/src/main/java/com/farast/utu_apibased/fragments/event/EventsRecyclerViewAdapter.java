package com.farast.utu_apibased.fragments.event;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farast.utu_apibased.R;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utuapi.data.Event;
import com.farast.utuapi.util.DateUtil;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Event} and makes a call to the
 * specified {@link OnListFragmentInteractionListener<Event> }.
 */
public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {

    private final List<Event> mValues;
    private final OnListFragmentInteractionListener<Event> mListener;

    public EventsRecyclerViewAdapter(List<Event> items, OnListFragmentInteractionListener<Event> listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Event event = mValues.get(position);;
        holder.mItem = event;
        holder.mTitleView.setText(event.getTitle());
        holder.mDateView.setText(DateUtil.CZ_DATE_FORMAT.format(event.getStart()));

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
        public Event mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.event_title);
            mDateView = (TextView) view.findViewById(R.id.event_date);
        }
    }
}
