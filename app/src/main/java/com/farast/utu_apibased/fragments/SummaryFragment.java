package com.farast.utu_apibased.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utuapi.data.User;

/**
 * Created by cendr_000 on 21.08.2016.
 */

public class SummaryFragment extends Fragment {

    private TextView mCurrentUserView;
    private Button mActionEventsView;
    private Button mActionTEsView;
    private Button mActionTimetablesView;

    public SummaryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        mCurrentUserView = (TextView) view.findViewById(R.id.summary_current_user);
        User user = Bullshit.dataLoader.getCurrentUser();
        if (user == null)
            mCurrentUserView.setText(getString(R.string.none_selected_class, Bullshit.dataLoader.getLastSclass().getName()));
        else
            mCurrentUserView.setText(user.getClassMember().getFullName());

        final OpenFragmentListener listener = (OpenFragmentListener) getActivity();

        mActionEventsView = (Button) view.findViewById(R.id.summary_action_events);
        mActionEventsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_events);
            }
        });
        mActionTEsView = (Button) view.findViewById(R.id.summary_action_tes);
        mActionTEsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_tes);
            }
        });
        mActionTimetablesView = (Button) view.findViewById(R.id.summary_action_timetable);
        mActionTimetablesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_timetables);
            }
        });

        return view;
    }

    public interface OpenFragmentListener {
        void setSelectedFragmentAndOpen(int id);
    }
}
