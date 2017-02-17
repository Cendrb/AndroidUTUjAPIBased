package com.farast.utu_apibased.tasks;

import android.content.Context;
import android.widget.Toast;

import com.farast.utu_apibased.Bullshit;
import com.farast.utuapi.data.interfaces.Hideable;
import com.farast.utuapi.exceptions.APIRequestException;

/**
 * Created by cendr on 16/02/2017.
 */

public class UtuHiderRevealer extends UtuSubmitter {

    private Hideable mItem;
    private boolean mHide;

    public UtuHiderRevealer(Context context, Hideable item, boolean hide) {
        super(context);
        mItem = item;
        mHide = hide;
    }

    @Override
    protected void executeInBackground() throws APIRequestException {
        if (mHide) {
            Bullshit.dataLoader.getHider().hideItem(mItem);
        } else {
            Bullshit.dataLoader.getHider().revealItem(mItem);
        }
    }

    @Override
    protected void showError(String string) {
        Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
    }
}
