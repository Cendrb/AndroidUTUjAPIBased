package com.farast.utu_apibased.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.farast.utu_apibased.R;
import com.farast.utuapi.exceptions.APIRequestException;
import com.farast.utuapi.exceptions.BothAdminRequiredException;
import com.farast.utuapi.exceptions.BothUserRequiredException;
import com.farast.utuapi.exceptions.ClientConnectionErrorException;
import com.farast.utuapi.exceptions.ClientPredataNotLoadedException;
import com.farast.utuapi.exceptions.ClientSclassDoesNotExistException;
import com.farast.utuapi.exceptions.ClientSclassUnknownException;
import com.farast.utuapi.exceptions.ClientUnexpectedResponseException;
import com.farast.utuapi.exceptions.ServerActiveRecordException;
import com.farast.utuapi.exceptions.ServerItemNotFoundException;
import com.farast.utuapi.exceptions.ServerUnexpectedRequestException;

/**
 * Created by cendr_000 on 18.08.2016.
 */

public abstract class UtuSubmitter extends AsyncTask<Void, Void, APIRequestException> {

    protected Context mContext;

    public UtuSubmitter(Context context) {
        mContext = context;
    }

    @Override
    protected final APIRequestException doInBackground(Void... voids) {
        try {
            executeInBackground();
            return null;
        } catch (APIRequestException e) {
            e.printStackTrace();
            return e;
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    protected final void onPostExecute(APIRequestException exception) {
        super.onPostExecute(exception);
        if (exception != null) {
            try {
                throw exception;
            } catch (BothAdminRequiredException e) {
                showError(mContext.getResources().getString(R.string.error_admin_required));
            } catch (BothUserRequiredException e) {
                showError(mContext.getResources().getString(R.string.error_user_required));
            } catch (ClientConnectionErrorException e) {
                showError(mContext.getResources().getString(R.string.error_failed_to_connect));
            } catch (ClientPredataNotLoadedException e) {
                showError(mContext.getResources().getString(R.string.error_internal_error));
            } catch (ClientSclassDoesNotExistException e) {
                showError(mContext.getResources().getString(R.string.error_internal_error));
            } catch (ClientSclassUnknownException e) {
                showError(mContext.getResources().getString(R.string.error_internal_error));
            } catch (ClientUnexpectedResponseException e) {
                showError(mContext.getResources().getString(R.string.error_internal_error));
            } catch (ServerActiveRecordException e) {
                showError(mContext.getResources().getString(R.string.error_internal_error));
            } catch (ServerItemNotFoundException e) {
                showError(mContext.getResources().getString(R.string.error_item_deleted));
            } catch (ServerUnexpectedRequestException e) {
                showError(mContext.getResources().getString(R.string.error_internal_error));
            } catch (APIRequestException e) {
                showError(mContext.getResources().getString(R.string.error_internal_error));
            } finally {
                onFinished(false);
            }
        } else {
            onFinished(true);
        }
    }

    @Override
    protected void onPreExecute() {

    }

    protected void onFinished(boolean success) {

    }

    protected void showError(String string) {

    }

    protected abstract void executeInBackground() throws APIRequestException;
}
