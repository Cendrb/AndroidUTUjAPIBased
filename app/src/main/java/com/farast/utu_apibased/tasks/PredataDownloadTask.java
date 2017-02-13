package com.farast.utu_apibased.tasks;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.farast.utu_apibased.R;
import com.farast.utu_apibased.ToStringConverter;
import com.farast.utu_apibased.custom_views.utu_spinner.UtuAdapter;
import com.farast.utuapi.data.Sclass;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

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

