package com.farast.utu_apibased.tasks;

import android.content.Context;

import com.farast.utuapi.exceptions.APIRequestException;

import static com.farast.utu_apibased.Bullshit.dataLoader;

/**
 * Created by cendr on 13/02/2017.
 */

public class UtuDataDownloader extends UtuSubmitter {

    private final int mSclassId;

    public UtuDataDownloader(Context context, int sclassId) {
        super(context);
        this.mSclassId = sclassId;
    }

    @Override
    protected final void executeInBackground() throws APIRequestException {
        dataLoader.load(mSclassId);
    }
}