package com.farast.utu_apibased.tasks;

import android.app.Activity;

import com.farast.utu_apibased.Bullshit;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.interfaces.Updatable;

import java.io.IOException;

/**
 * Created by cendr_000 on 18.08.2016.
 */

public class UtuDestroyer extends UtuSubmitter {

    private Activity sourceActivity;
    private Updatable item;

    public UtuDestroyer(Activity source, Updatable toBeDestroyed) {
        super(source);
        sourceActivity = source;
        item = toBeDestroyed;
    }

    @Override
    protected void onPreExecute() {
        sourceActivity.finish();
    }

    @Override
    protected String[] executeInBackground() throws IOException, DataLoader.AdminRequiredException, DataLoader.SclassUnknownException {
        return Bullshit.dataLoader.getEditor().requestDestroy(item);
    }
}
