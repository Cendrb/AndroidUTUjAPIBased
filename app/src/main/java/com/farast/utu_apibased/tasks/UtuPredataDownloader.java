package com.farast.utu_apibased.tasks;

import android.content.Context;

import com.farast.utuapi.exceptions.APIRequestException;

import static com.farast.utu_apibased.Bullshit.dataLoader;

/**
 * Created by cendr on 13/02/2017.
 */


public class UtuPredataDownloader extends UtuSubmitter {

    public UtuPredataDownloader(Context context) {
        super(context);
    }

    @Override
    protected final void executeInBackground() throws APIRequestException {
        dataLoader.loadPredata();
    }
}

