package com.farast.utu_apibased.adapters.generic_utu;

import android.content.Context;
import android.view.View;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.custom_views.UtuLineGenericViewHolder;
import com.farast.utuapi.data.Article;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.util.functional_interfaces.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class ArticlesAdapter extends GenericUtuAdapter<Article> {

    public ArticlesAdapter(Context context) {
        super(context, new Function<List<Article>>() {
            @Override
            public List<Article> execute() {
                return Bullshit.dataLoader.getArticlesList();
            }
        }, new ArrayList<DataLoader.EventType>() {{
            add(DataLoader.EventType.ARTICLES);
        }});
    }

    @Override
    protected void bindViewHolderToItem(UtuLineGenericViewHolder viewHolder, Article item) {
        viewHolder.mTitleView.setText(item.getTitle());
        viewHolder.mAvatarParent.setVisibility(View.GONE);
        if (item.isPublished()) {
            viewHolder.mLeftBottomView.setText(Bullshit.prettyDate(item.getPublishedOn()));
        } else {
            viewHolder.mLeftBottomView.setText(R.string.not_published);
        }
    }
}
