package com.farast.utu_apibased.tasks;

import android.os.AsyncTask;

import org.xml.sax.SAXException;

import java.io.IOException;

import static com.farast.utu_apibased.Bullshit.dataLoader;

/**
 * Created by cendr on 13/02/2017.
 */


public class PredataDownloadTask extends AsyncTask<Void, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            dataLoader.loadPredata();
            return true;
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

