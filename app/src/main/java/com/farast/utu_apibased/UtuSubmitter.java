package com.farast.utu_apibased;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.farast.utuapi.data.DataLoader;

import java.io.IOException;

/**
 * Created by cendr_000 on 18.08.2016.
 */

public abstract class UtuSubmitter extends AsyncTask<Void, Void, String[]> {

    protected Context mContext;

    public UtuSubmitter(Context context) {
        mContext = context;
    }

    @Override
    protected final String[] doInBackground(Void... voids) {
        try {
            return executeInBackground();
        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{mContext.getString(R.string.failed_to_connect)};
        } catch (DataLoader.AdminRequiredException e) {
            e.printStackTrace();
            return new String[]{mContext.getString(R.string.admin_required)};
        } catch (DataLoader.SclassUnknownException e) {
            e.printStackTrace();
            return new String[]{mContext.getString(R.string.sclass_not_set)};
        }
    }

    @Override
    protected abstract void onPreExecute();

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        if (strings == null)
            Toast.makeText(mContext, "Successfully submited", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(mContext, TextUtils.join(", ", strings), Toast.LENGTH_LONG).show();
        // TODO get better error notification system
    }

    protected abstract String[] executeInBackground() throws IOException, DataLoader.AdminRequiredException, DataLoader.SclassUnknownException;
}
