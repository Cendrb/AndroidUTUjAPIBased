package com.farast.utu_apibased.create_update_activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utuapi.data.Event;
import com.farast.utuapi.util.CollectionUtil;

public class CUEventActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuevent);

        int itemId = getIntent().getIntExtra("item_id", -1);
        if(itemId != -1)
        {
            Event loaded = CollectionUtil.findById(Bullshit.dataLoader.getEventsList(), itemId);
            Bullshit.dataLoader.getEditor().requestCUEvent(loaded,)
        }
        else
        {
            
        }
    }

}
