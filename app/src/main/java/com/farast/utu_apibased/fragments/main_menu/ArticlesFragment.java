package com.farast.utu_apibased.fragments.main_menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farast.utu_apibased.R;
import com.farast.utu_apibased.activities.show.ArticleShowActivity;
import com.farast.utu_apibased.adapters.generic_utu.ArticlesAdapter;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utu_apibased.util.ItemUtil;
import com.farast.utuapi.data.Article;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class ArticlesFragment extends Fragment {
    public ArticlesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.utu_list_recycler_view, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            ArticlesAdapter adapter = new ArticlesAdapter(context);
            adapter.setOnItemClickListener(new OnListFragmentInteractionListener<Article>() {
                @Override
                public void onListFragmentInteraction(Article item) {
                    Intent intent = new Intent(getContext(), ArticleShowActivity.class);
                    intent.putExtra(ItemUtil.ITEM_ID, item.getId());
                    getContext().startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}
