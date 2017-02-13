package com.farast.utu_apibased.activities.show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.farast.utu_apibased.activities.BindableViewHolder;
import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.util.ItemUtil;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.tasks.UtuDestroyer;
import com.farast.utu_apibased.activities.cu.CUExamActivity;
import com.farast.utu_apibased.activities.cu.CUTaskActivity;
import com.farast.utu_apibased.custom_views.additional_infos_viewer.AdditionalInfosViewer;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Exam;
import com.farast.utuapi.data.interfaces.TEItem;
import com.farast.utuapi.data.interfaces.Updatable;
import com.farast.utuapi.util.CollectionUtil;

/**
 * Created by cendr_000 on 14.08.2016.
 */

public class TEShowActivity extends AppCompatActivity implements DataLoader.OnDataSetListener {

    private int mItemId;
    private TEItem mTe;

    private ViewHolder mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItemId = ItemUtil.getItemIdFromIntent(getIntent());

        setContentView(R.layout.activity_show_te);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewHolder = new ViewHolder();
        mViewHolder.bindViewFields();

        onDataSetChanged();
        if (mTe instanceof Exam) {
            setTitle(R.string.item_exam);
            Bullshit.dataLoader.getNotifier().addListener(DataLoader.EventType.EXAMS, this);
        } else {
            setTitle(R.string.item_task);
            Bullshit.dataLoader.getNotifier().addListener(DataLoader.EventType.TASKS, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTe instanceof Exam) {
            Bullshit.dataLoader.getNotifier().removeListener(DataLoader.EventType.EXAMS, this);
        } else {
            Bullshit.dataLoader.getNotifier().removeListener(DataLoader.EventType.TASKS, this);
        }
    }

    @Override
    public void onDataSetChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mTe = CollectionUtil.findById(Bullshit.dataLoader.getTEsList(), mItemId);

                    mViewHolder.mTitleView.setText(mTe.getTitle());
                    mViewHolder.mDescriptionView.setText(Html.fromHtml(mTe.getDescription()));
                    mViewHolder.mAdditionalInfosViewerView.setInfos(mTe.getAdditionalInfos());
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
                Intent intent;
                if (mTe instanceof Exam)
                    intent = new Intent(this, CUExamActivity.class);
                else
                    intent = new Intent(this, CUTaskActivity.class);
                intent.putExtra("item_id", mTe.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_delete:
                new UtuDestroyer(this, (Updatable) mTe).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ViewHolder implements BindableViewHolder {
        private TextView mTitleView;
        private TextView mDescriptionView;
        private AdditionalInfosViewer mAdditionalInfosViewerView;

        @Override
        public void bindViewFields() {
            mTitleView = (TextView) findViewById(R.id.te_title);
            mDescriptionView = (TextView) findViewById(R.id.te_description);
            mAdditionalInfosViewerView = (AdditionalInfosViewer) findViewById(R.id.te_additional_infos);
        }
    }
}
