package com.farast.utu_apibased.show_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.ItemIdNotSuppliedException;
import com.farast.utu_apibased.R;
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
        int itemId = getIntent().getIntExtra("article_id", -1);
        if(itemId == -1)
            throw new ItemIdNotSuppliedException("Item id is not stored in this Intent");

        article = CollectionUtil.findById(Bullshit.dataLoader.getArticlesList(), itemId);


        setContentView(R.layout.activity_article_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // adds in up arrow, but relaunches MainActivity = bad
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.article_title);
        TextView description = (TextView) findViewById(R.id.article_description);

        title.setText(article.getTitle());
        description.setText(article.getDescription());
    }
}
