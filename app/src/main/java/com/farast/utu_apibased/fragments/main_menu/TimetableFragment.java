package com.farast.utu_apibased.fragments.main_menu;

import android.content.Context;
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
import com.farast.utu_apibased.adapters.TimetableAdapter;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.SchoolDay;
import com.farast.utuapi.data.Timetable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class TimetableFragment extends Fragment {

    private TimetableAdapter mAdapter;
    private List<Timetable> mAvailableTimetables;
    private int mSelectedItem = 0;
    private boolean showNextWeek = false;
    private DataLoader.OnDataSetListener mDataSetListener;

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

        View view = inflater.inflate(R.layout.fragment_timetable_grid, container, false);

        mAdapter = new TimetableAdapter(getActivity());
        // choose the best timetable if logged in
        if (Bullshit.dataLoader.getCurrentUser() != null) {
            mSelectedItem = mAvailableTimetables.indexOf(Timetable.getBestTimetableForClassMember(mAvailableTimetables, Bullshit.dataLoader.getCurrentUser().getClassMember()));
        }
        updateTimetableRender();

        GridView gridView = (GridView) view.findViewById(R.id.timetable_grid);
        gridView.setAdapter(mAdapter);

        final Handler handler = new Handler(getActivity().getMainLooper());
        mDataSetListener = new DataLoader.OnDataSetListener() {
            @Override
            public void onDataSetChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAvailableTimetables.clear();
                        mAvailableTimetables.addAll(Bullshit.dataLoader.getTimetablesList());
                        updateTimetableRender();
                    }
                });
            }
        };

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bullshit.dataLoader.getNotifier().addListener(DataLoader.EventType.TIMETABLES, mDataSetListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Bullshit.dataLoader.getNotifier().removeListener(DataLoader.EventType.TIMETABLES, mDataSetListener);
    }

    private void updateTimetableRender() {
        List<SchoolDay> sourceSchoolDays = mAvailableTimetables.get(mSelectedItem).getSchoolDays();
        ArrayList<SchoolDay> resultSchoolDays = new ArrayList<>();
        if (sourceSchoolDays.get(0) == null)
            return;
        Date firstDayDate = sourceSchoolDays.get(0).getDate();
        if (showNextWeek) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(firstDayDate);
            calendar.add(Calendar.DATE, 6);
            Date lastDayOfFirstWeek = calendar.getTime();

            calendar.setTime(firstDayDate);
            calendar.add(Calendar.DATE, 14);
            Date dayAfterSecondWeek = calendar.getTime();

            for (SchoolDay schoolDay : sourceSchoolDays) {
                if (schoolDay.getDate().after(lastDayOfFirstWeek) && schoolDay.getDate().before(dayAfterSecondWeek)) {
                    resultSchoolDays.add(schoolDay);
                }
            }
        } else {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(firstDayDate);
            calendar.add(Calendar.DATE, -1);
            Date dayBeforeFirstWeek = calendar.getTime();

            calendar.setTime(firstDayDate);
            calendar.add(Calendar.DATE, 7);
            Date dayAfterFirstWeek = calendar.getTime();

            for (SchoolDay schoolDay : sourceSchoolDays) {
                if (schoolDay.getDate().after(dayBeforeFirstWeek) && schoolDay.getDate().before(dayAfterFirstWeek)) {
                    resultSchoolDays.add(schoolDay);
                }
            }
        }
        mAdapter.setSchoolDayData(resultSchoolDays);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_timetable, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_timetables_select) {
            ArrayAdapter<Timetable> timetablesAdapter = new ArrayAdapter<Timetable>(getActivity(), android.R.layout.simple_list_item_single_choice);
            timetablesAdapter.addAll(mAvailableTimetables);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setSingleChoiceItems(timetablesAdapter, mSelectedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mSelectedItem = i;
                    updateTimetableRender();
                    dialogInterface.dismiss();
                }
            });
            builder.show();
            return true;
        } else if (item.getItemId() == R.id.menu_item_timetables_switch_week) {
            showNextWeek = !showNextWeek;
            if (showNextWeek) {
                item.setIcon(R.drawable.ic_navigate_before);
                item.setTitle(R.string.nav_previous_week);
            } else {
                item.setIcon(R.drawable.ic_navigate_next);
                item.setTitle(R.string.nav_next_week);
            }
            updateTimetableRender();
        }
        return super.onOptionsItemSelected(item);
    }
}
