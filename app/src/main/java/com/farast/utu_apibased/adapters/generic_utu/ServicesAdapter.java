package com.farast.utu_apibased.adapters.generic_utu;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.custom_views.UtuLineGenericViewHolder;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Service;
import com.farast.utuapi.util.CollectionUtil;
import com.farast.utuapi.util.DateUtil;
import com.farast.utuapi.util.functional_interfaces.Function;
import com.farast.utuapi.util.functional_interfaces.Predicate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cendr on 09/02/2017.
 */

public class ServicesAdapter extends GenericUtuAdapter<Service> {

    public ServicesAdapter(Context context) {
        super(context, new Function<List<Service>>() {
            @Override
            public List<Service> execute() {
                return CollectionUtil.filter(Bullshit.dataLoader.getServices(), new Predicate<Service>() {
                    @Override
                    public boolean test(Service service) {
                        return service.getEnd().after(new Date());
                    }
                });
            }
        }, new ArrayList<DataLoader.EventType>() {{
            add(DataLoader.EventType.LOAD_FINISHED);
        }});
    }

    @Override
    protected void bindViewHolderToItem(UtuLineGenericViewHolder viewHolder, Service item) {
        boolean highlightAsMine = false;
        if (Bullshit.dataLoader.getCurrentUser() != null) {
            highlightAsMine = item.getFirstMember() == Bullshit.dataLoader.getCurrentUser().getClassMember() ||
                    item.getSecondMember() == Bullshit.dataLoader.getCurrentUser().getClassMember();
        }

        viewHolder.mTitleView.setText(item.getFirstMember().getFullName() + " " + mContext.getString(R.string.and) + " " + item.getSecondMember().getFullName());
        if (highlightAsMine) {
            viewHolder.mTitleView.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            viewHolder.mTitleView.setTypeface(Typeface.DEFAULT);
        }
        viewHolder.mAvatarParent.setVisibility(View.GONE);
        viewHolder.mLeftBottomView.setText(DateUtil.CZ_SHORT_DATE_FORMAT.format(item.getStart()) + " - " + DateUtil.CZ_SHORT_DATE_FORMAT.format(item.getEnd()));
    }
}
