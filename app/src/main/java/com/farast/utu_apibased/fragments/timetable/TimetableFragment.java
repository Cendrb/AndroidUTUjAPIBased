package com.farast.utu_apibased.fragments.timetable;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Timetable;

import java.util.List;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class TimetableFragment extends Fragment {

    TimetableAdapter mAdapter;
    List<Timetable> mAvailableTimetables;
    int mSelectedItem = 0;

    public TimetableFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAvailableTimetables = Bullshit.dataLoader.getTimetablesList();

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.timetable_grid, container, false);

        mAdapter = new TimetableAdapter(getActivity());
        mAdapter.setTimetable(mAvailableTimetables.get(mSelectedItem));

        GridView gridView = (GridView) view.findViewById(R.id.timetable_grid);
        gridView.setAdapter(mAdapter);

        final Handler handler = new Handler(getActivity().getMainLooper());
        Bullshit.dataLoader.getNotifier().setTimetablesListener(new DataLoader.OnDataSetListener() {
            @Override
            public void onDataSetChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAvailableTimetables.clear();
                        mAvailableTimetables.addAll(Bullshit.dataLoader.getTimetablesList());
                        mAdapter.setTimetable(mAvailableTimetables.get(mSelectedItem));
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_timetable, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_timetables_select)
        {
            ArrayAdapter<Timetable> timetablesAdapter = new ArrayAdapter<Timetable>(getActivity(), android.R.layout.simple_list_item_single_choice);
            timetablesAdapter.addAll(mAvailableTimetables);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setSingleChoiceItems(timetablesAdapter, mSelectedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mSelectedItem = i;
                    mAdapter.setTimetable(mAvailableTimetables.get(mSelectedItem));
                    dialogInterface.dismiss();
                }
            });
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
