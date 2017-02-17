package com.farast.utu_apibased.tasks;

import android.os.AsyncTask;

import com.farast.utuapi.exceptions.PredataNotLoadedException;
import com.farast.utuapi.util.SclassDoesNotExistException;

import org.xml.sax.SAXException;

import java.io.IOException;

import static com.farast.utu_apibased.Bullshit.dataLoader;

/**
 * Created by cendr on 13/02/2017.
 */

public class DataDownloadTask extends AsyncTask<DataDownloadTask.Params, Void, Boolean> {

    protected Params params;

    @Override
    protected Boolean doInBackground(Params... paramses) {
        this.params = paramses[0];
        try {
            dataLoader.load(params.getSclassId());
            return true;
        } catch (PredataNotLoadedException | SclassDoesNotExistException | SAXException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class Params {
        private final int sclassId;

        public Params(int sclassId) {
            this.sclassId = sclassId;
        }

        public int getSclassId() {
            return sclassId;
        }
    }
}