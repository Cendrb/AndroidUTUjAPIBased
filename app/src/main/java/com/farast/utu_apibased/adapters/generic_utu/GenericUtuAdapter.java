package com.farast.utu_apibased.adapters.generic_utu;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.custom_views.UtuLineGenericViewHolder;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.util.functional_interfaces.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr on 14/02/2017.
 */

public abstract class GenericUtuAdapter<TItemType> extends RecyclerView.Adapter<UtuLineGenericViewHolder> {

    protected Context mContext;
    private ArrayList<TItemType> mItems = new ArrayList<>();
    private DataLoader.OnDataSetListener mDataSetChangedListener;
    private final List<DataLoader.EventType> mEventsToListenFor;
    private OnListFragmentInteractionListener<TItemType> mOnClickListener;
    private Function<List<TItemType>> mItemsGetter;

    public GenericUtuAdapter(Context context, Function<List<TItemType>> itemsGetter, List<DataLoader.EventType> eventsToListenFor) {
        super();

        mContext = context;
        mEventsToListenFor = eventsToListenFor;
        mItemsGetter = itemsGetter;

        final Handler handler = new Handler(context.getMainLooper());
        mDataSetChangedListener = new DataLoader.OnDataSetListener() {
            @Override
            public void onDataSetChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        reloadItemsAndNotifyDataSetChange();
                    }
                });
            }
        };

        mDataSetChangedListener.onDataSetChanged();
    }

    protected void setItemsGetter(Function<List<TItemType>> itemsGetter) {
        mItemsGetter = itemsGetter;
    }

    protected void reloadItemsAndNotifyDataSetChange() {
        mItems.clear();
        mItems.addAll(mItemsGetter.execute());
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnListFragmentInteractionListener<TItemType> listener) {
        mOnClickListener = listener;
    }

    @Override
    public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
        for (DataLoader.EventType eventType : mEventsToListenFor) {
            Bullshit.dataLoader.getNotifier().addListener(eventType, mDataSetChangedListener);
        }
    }

    @Override
    public final void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        for (DataLoader.EventType eventType : mEventsToListenFor) {
            Bullshit.dataLoader.getNotifier().removeListener(eventType, mDataSetChangedListener);
        }
    }

    @Override
    public final UtuLineGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.utu_line_generic, parent, false);
        return new UtuLineGenericViewHolder(view);
    }

    @Override
    public final void onBindViewHolder(UtuLineGenericViewHolder holder, int position) {
        final TItemType item = mItems.get(position);
        bindViewHolderToItem(holder, item);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mOnClickListener.onListFragmentInteraction(item);
                }
            }
        });
    }

    @Override
    public final int getItemCount() {
        return mItems.size();
    }

    protected abstract void bindViewHolderToItem(UtuLineGenericViewHolder viewHolder, TItemType item);


}
