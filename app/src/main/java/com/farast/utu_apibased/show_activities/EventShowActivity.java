package com.farast.utu_apibased.show_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.farast.utu_apibased.BindableViewHolder;
import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.ItemUtil;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.UtuDestroyer;
import com.farast.utu_apibased.create_update_activities.CUEventActivity;
import com.farast.utu_apibased.custom_views.additional_infos_viewer.AdditionalInfosViewer;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Event;
import com.farast.utuapi.util.CollectionUtil;

public class EventShowActivity extends AppCompatActivity implements DataLoader.OnDataSetListener {

    private Event mEvent;
    private int mItemId;

    private ViewHolder mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItemId = ItemUtil.getItemIdFromIntent(getIntent());

        setContentView(R.layout.activity_show_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewHolder = new ViewHolder();
        mViewHolder.bindViewFields();

        onDataSetChanged();

        setTitle(R.string.item_event);

        Bullshit.dataLoader.getNotifier().addListener(DataLoader.EventType.EVENTS, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bullshit.dataLoader.getNotifier().removeListener(DataLoader.EventType.EVENTS, this);
    }

    @Override
    public void onDataSetChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mEvent = CollectionUtil.findById(Bullshit.dataLoader.getEventsList(), mItemId);

                    mViewHolder.mTitleView.setText(mEvent.getTitle());
                    mViewHolder.mDescriptionView.setText(mEvent.getDescription());
                    mViewHolder.mLocationView.setText(mEvent.getLocation());
                    mViewHolder.mAdditionalInfosView.setInfos(mEvent.getAdditionalInfos());
                } catch (CollectionUtil.RecordNotFoundException e) {
                    // record was deleted
                }
            }
        });
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
                intent.putExtra("item_id", mEvent.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_delete:
                new UtuDestroyer(this, mEvent).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ViewHolder implements BindableViewHolder {
        private TextView mTitleView;
        private TextView mDescriptionView;
        private TextView mLocationView;
        private AdditionalInfosViewer mAdditionalInfosView;

        @Override
        public void bindViewFields() {
            mTitleView = (TextView) findViewById(R.id.show_title);
            mDescriptionView = (TextView) findViewById(R.id.show_description);
            mLocationView = (TextView) findViewById(R.id.event_location);
            mAdditionalInfosView = (AdditionalInfosViewer) findViewById(R.id.event_additional_infos);
        }
    }
}
