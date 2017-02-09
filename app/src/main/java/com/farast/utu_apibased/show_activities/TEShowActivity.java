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
import com.farast.utu_apibased.create_update_activities.CUExamActivity;
import com.farast.utu_apibased.create_update_activities.CUTaskActivity;
import com.farast.utu_apibased.custom_views.additional_infos_viewer.AdditionalInfosViewer;
import com.farast.utuapi.data.Exam;
import com.farast.utuapi.data.interfaces.TEItem;
import com.farast.utuapi.data.interfaces.Updatable;
import com.farast.utuapi.util.CollectionUtil;

/**
 * Created by cendr_000 on 14.08.2016.
 */

public class TEShowActivity extends AppCompatActivity {
    TEItem te;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null)
            throw new ItemIdNotSuppliedException("Intent is null");
        int itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId == -1)
            throw new ItemIdNotSuppliedException("Item id is not stored in this Intent");

        te = CollectionUtil.findById(Bullshit.dataLoader.getTEsList(), itemId);


        setContentView(R.layout.activity_show_te);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(te.getTitle());

        TextView title = (TextView) findViewById(R.id.te_title);
        TextView description = (TextView) findViewById(R.id.te_description);
        AdditionalInfosViewer additionalInfosViewer = (AdditionalInfosViewer) findViewById(R.id.te_additional_infos);

        title.setText(te.getTitle());
        description.setText(te.getDescription());
        additionalInfosViewer.setInfos(te.getAdditionalInfos());
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
                Intent intent;
                if (te instanceof Exam)
                    intent = new Intent(this, CUExamActivity.class);
                else
                    intent = new Intent(this, CUTaskActivity.class);
                intent.putExtra("item_id", te.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_delete:
                new UtuDestroyer(this, (Updatable) te).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
