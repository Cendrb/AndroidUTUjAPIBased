package com.farast.utu_apibased.adapters.generic_utu;

import android.content.Context;
import android.view.View;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.custom_views.UtuLineGenericViewHolder;
import com.farast.utu_apibased.util.DateOffseter;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Event;
import com.farast.utuapi.util.functional_interfaces.Function;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends GenericUtuAdapter<Event> {

    public EventsAdapter(Context context) {
        super(context, new Function<List<Event>>() {
            @Override
            public List<Event> execute() {
                return Bullshit.dataLoader.getEventsList();
            }
        }, new ArrayList<DataLoader.EventType>() {{
            add(DataLoader.EventType.EVENTS);
        }});
    }

    @Override
    protected void bindViewHolderToItem(UtuLineGenericViewHolder viewHolder, Event item) {
        viewHolder.mTitleView.setText(item.getTitle());
        viewHolder.mAvatarParent.setVisibility(View.GONE);
        viewHolder.mLeftBottomView.setText(Bullshit.prettyDate(DateOffseter.addRegularDateOffset(item.getStart())));
    }
}
