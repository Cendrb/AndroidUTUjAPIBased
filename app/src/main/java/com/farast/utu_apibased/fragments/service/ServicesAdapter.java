package com.farast.utu_apibased.fragments.service;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.UtuLineGenericViewHolder;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Service;
import com.farast.utuapi.util.CollectionUtil;
import com.farast.utuapi.util.DateUtil;
import com.farast.utuapi.util.functional_interfaces.Predicate;

import java.util.Date;
import java.util.List;

/**
 * Created by cendr on 09/02/2017.
 */

public class ServicesAdapter extends RecyclerView.Adapter<UtuLineGenericViewHolder> {

    private Context mContext;

    private List<Service> mServices;
    private DataLoader.OnDataSetListener mDataSetListener;

    public ServicesAdapter(Context context) {
        mContext = context;
        // filter out previous ones
        mServices = CollectionUtil.filter(Bullshit.dataLoader.getServices(), new Predicate<Service>() {
            @Override
            public boolean test(Service service) {
                return service.getEnd().after(new Date());
            }
        });

        final Handler handler = new Handler(context.getMainLooper());

        mDataSetListener = new DataLoader.OnDataSetListener() {
            @Override
            public void onDataSetChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mServices.clear();
                        mServices.addAll(Bullshit.dataLoader.getServices());
                        notifyDataSetChanged();
                    }
                });
            }
        };
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        Bullshit.dataLoader.getNotifier().addListener(DataLoader.EventType.LOAD_FINISHED, mDataSetListener);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        Bullshit.dataLoader.getNotifier().removeListener(DataLoader.EventType.LOAD_FINISHED, mDataSetListener);
    }

    @Override
    public UtuLineGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.utu_line_generic, parent, false);
        return new UtuLineGenericViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UtuLineGenericViewHolder holder, int position) {
        final Service item = mServices.get(position);
        holder.mTitleView.setText(item.getFirstMember().getFullName() + " " + mContext.getString(R.string.and) + " " + item.getSecondMember().getFullName());
        holder.mAvatarParent.setVisibility(View.GONE);
        holder.mLeftBottomView.setText(DateUtil.CZ_SHORT_DATE_FORMAT.format(item.getStart()) + " - " + DateUtil.CZ_SHORT_DATE_FORMAT.format(item.getEnd()));
    }

    @Override
    public int getItemCount() {
        return mServices.size();
    }
}
