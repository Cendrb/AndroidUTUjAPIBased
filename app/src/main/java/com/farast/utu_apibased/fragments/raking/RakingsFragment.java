package com.farast.utu_apibased.fragments.raking;

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
import com.farast.utu_apibased.fragments.event.EventsRecyclerViewAdapter;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utu_apibased.show_activities.EventShowActivity;
import com.farast.utu_apibased.show_activities.PlannedRakingListShowActivity;
import com.farast.utu_apibased.show_activities.TEShowActivity;
import com.farast.utuapi.data.Event;
import com.farast.utuapi.data.PlannedRakingList;

/**
 * Created by cendr_000 on 27.08.2016.
 */

public class RakingsFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.utu_list_fragment_something, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new RakingsRecyclerViewAdapter(new OnListFragmentInteractionListener<PlannedRakingList>() {
                @Override
                public void onListFragmentInteraction(PlannedRakingList item) {
                    Intent intent = new Intent(getContext(), PlannedRakingListShowActivity.class);
                    intent.putExtra("item_id", item.getId());
                    getContext().startActivity(intent);
                }
            }, context));
        }
        return view;
    }
}
