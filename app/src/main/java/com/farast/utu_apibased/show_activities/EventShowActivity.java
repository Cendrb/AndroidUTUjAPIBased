package com.farast.utu_apibased.show_activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.ItemIdNotSuppliedException;
import com.farast.utu_apibased.R;
import com.farast.utuapi.data.Event;
import com.farast.utuapi.util.CollectionUtil;

public class EventShowActivity extends AppCompatActivity {

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null)
            throw new ItemIdNotSuppliedException("Intent is null");
        int itemId = getIntent().getIntExtra("event_id", -1);
        if(itemId == -1)
            throw new ItemIdNotSuppliedException("Item id is not stored in this Intent");

        event = CollectionUtil.findById(Bullshit.dataLoader.getEventsList(), itemId);


        setContentView(R.layout.activity_event_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // adds in up arrow, but relaunches MainActivity = bad
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.event_title);
        TextView description = (TextView) findViewById(R.id.event_description);
        TextView location = (TextView) findViewById(R.id.event_location);

        title.setText(event.getTitle());
        description.setText(event.getDescription());
        location.setText(event.getLocation());
    }

}
