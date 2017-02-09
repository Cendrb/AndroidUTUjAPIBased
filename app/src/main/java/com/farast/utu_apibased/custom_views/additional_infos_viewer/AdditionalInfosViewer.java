package com.farast.utu_apibased.custom_views.additional_infos_viewer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.farast.utu_apibased.custom_views.utu_clickable_item_list.UtuItemsClickableAdapter;
import com.farast.utuapi.data.AdditionalInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr on 09/02/2017.
 */

public class AdditionalInfosViewer extends RecyclerView {

    private UtuItemsClickableAdapter<AdditionalInfo> mAdapter;

    public AdditionalInfosViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new UtuItemsClickableAdapter<AdditionalInfo>(new ArrayList<AdditionalInfo>());
        mAdapter.setItemSelectedListener(new UtuItemsClickableAdapter.OnItemSelectedListener<AdditionalInfo>() {
            @Override
            public void onItemSelected(AdditionalInfo item) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(item.getUrl()));
                getContext().startActivity(intent);
            }
        });
        setAdapter(mAdapter);
    }

    public void setInfos(List<AdditionalInfo> infos) {
        mAdapter.setItems(infos);
    }
}
