package com.farast.utu_apibased.fragments.article;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.fragments.te.TEsRecyclerViewAdapter;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utuapi.data.Article;
import com.farast.utuapi.data.TEItem;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class ArticlesFragment extends Fragment {
    public ArticlesFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ArticlesRecyclerViewFragment(Bullshit.dataLoader.getArticlesList(), new OnListFragmentInteractionListener<Article>() {
                @Override
                public void onListFragmentInteraction(Article item) {
                    Snackbar.make(getView(), "TEItem show screen", 3).show();
                }
            }));
        }
        return view;
    }
}
