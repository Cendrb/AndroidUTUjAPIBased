package com.farast.utu_apibased.fragments.timetable;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.fragments.te.TEsRecyclerViewAdapter;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utuapi.data.TEItem;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class TimetableFragment extends Fragment {
    public TimetableFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timetable_grid, container, false);

        Context context = view.getContext();
        GridView gridView = (GridView) view.findViewById(R.id.timetable_grid);
        gridView.setAdapter(new TimetableAdapter(Bullshit.dataLoader.getTimetablesList().get(0), context));
        return view;
    }
}
