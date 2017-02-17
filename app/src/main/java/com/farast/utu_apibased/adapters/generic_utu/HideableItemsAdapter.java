package com.farast.utu_apibased.adapters.generic_utu;

import android.content.Context;
import android.view.View;

import com.farast.utu_apibased.R;
import com.farast.utu_apibased.custom_views.UtuLineGenericViewHolder;
import com.farast.utu_apibased.tasks.UtuHiderRevealer;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.interfaces.Hideable;
import com.farast.utuapi.util.CollectionUtil;
import com.farast.utuapi.util.functional_interfaces.Function;
import com.farast.utuapi.util.functional_interfaces.Predicate;

import java.util.List;

/**
 * Created by cendr on 16/02/2017.
 */

public abstract class HideableItemsAdapter<TItemType extends Hideable> extends GenericUtuAdapter<TItemType> {

    private boolean mShowAll = false;

    public HideableItemsAdapter(Context context, final Function<List<TItemType>> itemsGetter, List<DataLoader.EventType> eventsToListenFor) {
        super(context, new Function<List<TItemType>>() {
            @Override
            public List<TItemType> execute() {
                return CollectionUtil.filter(itemsGetter.execute(), new Predicate<TItemType>() {
                    @Override
                    public boolean test(TItemType tItemType) {
                        return !tItemType.isDone();
                    }
                });
            }
        }, eventsToListenFor);

        // unable to use instance variables in super call - set the proper items getter later
        setItemsGetter(new Function<List<TItemType>>() {
            @Override
            public List<TItemType> execute() {
                return CollectionUtil.filter(itemsGetter.execute(), new Predicate<TItemType>() {
                    @Override
                    public boolean test(TItemType tItemType) {
                        return mShowAll || !tItemType.isDone();
                    }
                });
            }
        });
    }

    @Override
    protected void bindViewHolderToItem(UtuLineGenericViewHolder viewHolder, final TItemType item) {
        if (mShowAll) {
            viewHolder.mRightIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.isDone()) {
                        new UtuHiderRevealer(mContext, item, false).execute();
                    } else {
                        new UtuHiderRevealer(mContext, item, true).execute();
                    }
                }
            });
            if (item.isDone()) {
                viewHolder.mRightIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_visibility_on));
            } else {
                viewHolder.mRightIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_visibility_off));
            }
        } else {
            viewHolder.mRightIcon.setBackground(null);
            viewHolder.mRightIcon.setOnClickListener(null);
        }
    }

    public void setShowAll(boolean showAll) {
        mShowAll = showAll;
        reloadItemsAndNotifyDataSetChange();
    }

    public boolean isShowAll() {
        return mShowAll;
    }
}
