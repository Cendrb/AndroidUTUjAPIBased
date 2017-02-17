package com.farast.utu_apibased.tasks;

import android.content.Context;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utuapi.data.interfaces.Hideable;
import com.farast.utuapi.exceptions.AdminRequiredException;
import com.farast.utuapi.exceptions.SclassUnknownException;
import com.farast.utuapi.exceptions.UserRequiredException;

import java.io.IOException;

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
    protected void onPreExecute() {

    }

    @Override
    protected String[] executeInBackground() throws IOException, AdminRequiredException, SclassUnknownException, UserRequiredException {
        boolean result;
        if (mHide) {
            result = Bullshit.dataLoader.getHider().hideItem(mItem);
        } else {
            result = Bullshit.dataLoader.getHider().revealItem(mItem);
        }
        if (result) {
            return null;
        } else {
            return new String[]{mContext.getResources().getString(R.string.failed_to_connect)};
        }
    }
}
