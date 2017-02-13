package com.farast.utu_apibased.show_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.farast.utu_apibased.BindableViewHolder;
import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.ItemUtil;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.UtuDestroyer;
import com.farast.utu_apibased.create_update_activities.CUArticleActivity;
import com.farast.utu_apibased.custom_views.additional_infos_viewer.AdditionalInfosViewer;
import com.farast.utuapi.data.Article;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.util.CollectionUtil;

/**
 * Created by cendr_000 on 14.08.2016.
 */

public class ArticleShowActivity extends AppCompatActivity implements DataLoader.OnDataSetListener {

    private int mItemId;
    private Article mArticle;

    private ViewHolder mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItemId = ItemUtil.getItemIdFromIntent(getIntent());

        setContentView(R.layout.activity_show_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewHolder = new ViewHolder();
        mViewHolder.bindViewFields();

        onDataSetChanged();

        setTitle(R.string.item_article);

        Bullshit.dataLoader.getNotifier().addListener(DataLoader.EventType.ARTICLES, this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bullshit.dataLoader.getNotifier().removeListener(DataLoader.EventType.ARTICLES, this);
    }

    @Override
    public void onDataSetChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mArticle = CollectionUtil.findById(Bullshit.dataLoader.getArticlesList(), mItemId);

                    mViewHolder.mTitleView.setText(mArticle.getTitle());
                    mViewHolder.mDescriptionView.setText(Html.fromHtml(mArticle.getDescription()));
                    mViewHolder.mAdditionalInfosViewerView.setInfos(mArticle.getAdditionalInfos());
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
                Intent intent = new Intent(this, CUArticleActivity.class);
                intent.putExtra("item_id", mArticle.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_delete:
                new UtuDestroyer(this, mArticle).execute();
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
            mTitleView = (TextView) findViewById(R.id.article_title);
            mDescriptionView = (TextView) findViewById(R.id.article_description);
            mAdditionalInfosViewerView = (AdditionalInfosViewer) findViewById(R.id.article_additional_infos);
        }
    }
}
