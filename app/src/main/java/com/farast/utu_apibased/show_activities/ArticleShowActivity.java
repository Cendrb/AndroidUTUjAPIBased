package com.farast.utu_apibased.show_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.ItemIdNotSuppliedException;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.UtuDestroyer;
import com.farast.utu_apibased.create_update_activities.CUArticleActivity;
import com.farast.utuapi.data.Article;
import com.farast.utuapi.util.CollectionUtil;

/**
 * Created by cendr_000 on 14.08.2016.
 */

public class ArticleShowActivity extends AppCompatActivity {
    Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null)
            throw new ItemIdNotSuppliedException("Intent is null");
        int itemId = getIntent().getIntExtra("item_id", -1);
        if (itemId == -1)
            throw new ItemIdNotSuppliedException("Item id is not stored in this Intent");

        article = CollectionUtil.findById(Bullshit.dataLoader.getArticlesList(), itemId);


        setContentView(R.layout.activity_show_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(article.getTitle());

        TextView title = (TextView) findViewById(R.id.article_title);
        TextView description = (TextView) findViewById(R.id.article_description);

        title.setText(article.getTitle());
        description.setText(Html.fromHtml(article.getDescription()));
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
                intent.putExtra("item_id", article.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_delete:
                new UtuDestroyer(this, article).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
