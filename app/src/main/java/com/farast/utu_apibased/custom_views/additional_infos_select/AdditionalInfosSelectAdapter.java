package com.farast.utu_apibased.custom_views.additional_infos_select;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utuapi.data.AdditionalInfo;
import com.farast.utuapi.data.Subject;
import com.farast.utuapi.util.CollectionUtil;
import com.farast.utuapi.util.functional_interfaces.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr_000 on 19.08.2016.
 */

public class AdditionalInfosSelectAdapter extends RecyclerView.Adapter<AdditionalInfosSelectAdapter.ViewHolder> {

    private final List<AdditionalInfo> mAllAIs;
    private final List<AdditionalInfo> mAIsSelectedSourceList;
    private OnInfoSelectedListener mInfoSelectedListener;

    public AdditionalInfosSelectAdapter(Context context, List<AdditionalInfo> currentlySelectedInfos) {
        mAllAIs = new ArrayList<>();
        mAIsSelectedSourceList = currentlySelectedInfos;
    }

    public void setSubjectFilter(final Subject subject) {
        mAllAIs.clear();
        mAllAIs.addAll(CollectionUtil.filter(Bullshit.dataLoader.getAdditionalInfosList(), new Predicate<AdditionalInfo>() {
            @Override
            public boolean test(AdditionalInfo additionalInfo) {
                return additionalInfo.getSubject().equals(subject);
            }
        }));
        notifyDataSetChanged();
    }

    @Override
    public AdditionalInfosSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        return new AdditionalInfosSelectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdditionalInfosSelectAdapter.ViewHolder holder, int position) {
        final AdditionalInfo currentlyRenderedInfo = mAllAIs.get(position);
        holder.mView.setText(currentlyRenderedInfo.getName());

        holder.mView.setChecked(mAIsSelectedSourceList.contains(currentlyRenderedInfo));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                checkedTextView.toggle();
                if (mInfoSelectedListener != null) {
                    mInfoSelectedListener.onInfoSeleted(checkedTextView.isChecked(), currentlyRenderedInfo);
                }
            }
        });
    }

    public void setInfoSelectedListener(OnInfoSelectedListener mInfoSelectedListener) {
        this.mInfoSelectedListener = mInfoSelectedListener;
    }

    @Override
    public int getItemCount() {
        return mAllAIs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckedTextView mView;

        public ViewHolder(View view) {
            super(view);
            mView = (CheckedTextView) view.findViewById(android.R.id.text1);
        }
    }

    public interface OnInfoSelectedListener {
        void onInfoSeleted(boolean checked, AdditionalInfo info);
    }
}
