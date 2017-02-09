package com.farast.utu_apibased.show_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.ItemIdNotSuppliedException;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.UtuDestroyer;
import com.farast.utu_apibased.create_update_activities.CUEventActivity;
import com.farast.utu_apibased.custom_views.additional_infos_viewer.AdditionalInfosViewer;
import com.farast.utuapi.data.Event;
import com.farast.utuapi.util.CollectionUtil;

public class EventShowActivity extends AppCompatActivity {

    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null)
            throw new ItemIdNotSuppliedException("Intent is null");
        int itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId == -1)
            throw new ItemIdNotSuppliedException("Item id is not stored in this Intent");

        event = CollectionUtil.findById(Bullshit.dataLoader.getEventsList(), itemId);


        setContentView(R.layout.activity_show_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(event.getTitle());

        TextView title = (TextView) findViewById(R.id.show_title);
        TextView description = (TextView) findViewById(R.id.show_description);
        TextView location = (TextView) findViewById(R.id.event_location);
        AdditionalInfosViewer additionalInfosViewer = (AdditionalInfosViewer) findViewById(R.id.event_additional_infos);

        title.setText(event.getTitle());
        description.setText(event.getDescription());
        location.setText(event.getLocation());
        additionalInfosViewer.setInfos(event.getAdditionalInfos());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Bullshit.dataLoader.isAdminLoggedIn())
            getMenuInflater().inflate(R.menu.generic_utu_item_show_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit:
                Intent intent = new Intent(this, CUEventActivity.class);
                intent.putExtra("item_id", event.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_delete:
                new UtuDestroyer(this, event).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
