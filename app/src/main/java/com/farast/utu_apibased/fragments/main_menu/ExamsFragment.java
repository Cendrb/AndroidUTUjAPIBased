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
import com.farast.utu_apibased.activities.show.TEShowActivity;
import com.farast.utu_apibased.adapters.generic_utu.ExamsAdapter;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utu_apibased.util.ItemUtil;
import com.farast.utuapi.data.Exam;
import com.farast.utuapi.data.common.UtuType;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class ExamsFragment extends Fragment {

    public ExamsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.utu_list_recycler_view, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            ExamsAdapter adapter = new ExamsAdapter(context);
            adapter.setOnItemClickListener(new OnListFragmentInteractionListener<Exam>() {
                @Override
                public void onListFragmentInteraction(Exam item) {
                    Intent intent = new Intent(getContext(), TEShowActivity.class);
                    intent.putExtra(ItemUtil.ITEM_ID, item.getId());
                    intent.putExtra(ItemUtil.ITEM_TYPE, UtuType.EXAM.ordinal());
                    getContext().startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}
