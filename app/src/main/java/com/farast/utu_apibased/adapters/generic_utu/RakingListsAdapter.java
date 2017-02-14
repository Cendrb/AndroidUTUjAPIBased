package com.farast.utu_apibased.adapters.generic_utu;

import android.content.Context;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.custom_views.UtuLineGenericViewHolder;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.PlannedRakingList;
import com.farast.utuapi.util.functional_interfaces.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr_000 on 27.08.2016.
 */
public class RakingListsAdapter extends GenericUtuAdapter<PlannedRakingList> {

    public RakingListsAdapter(Context context) {
        super(context, new Function<List<PlannedRakingList>>() {
            @Override
            public List<PlannedRakingList> execute() {
                return Bullshit.dataLoader.getPlannedRakingsListsList();
            }
        }, new ArrayList<DataLoader.EventType>() {{
            add(DataLoader.EventType.RAKINGS);
        }});
    }

    @Override
    protected void bindViewHolderToItem(UtuLineGenericViewHolder viewHolder, PlannedRakingList item) {
        viewHolder.mTitleView.setText(mContext.getString(R.string.planned_raking_list_title, item.getTitle(), item.getCurrentRound().getRoundNumber()));
        viewHolder.mAvatarTextView.setText(item.getSubject().getName());
        viewHolder.mLeftBottomView.setText(mContext.getResources()
                .getQuantityString(
                        R.plurals.planned_raking_list_description,
                        item.getCurrentRound().getSignedUpRemainingCount(),
                        item.getCurrentRound().getSignedUpRemainingCount()));
    }
}
