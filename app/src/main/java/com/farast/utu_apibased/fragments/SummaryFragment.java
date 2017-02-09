package com.farast.utu_apibased.fragments;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utuapi.data.ClassMember;
import com.farast.utuapi.data.User;

import java.util.List;
import java.util.Random;

/**
 * Created by cendr_000 on 21.08.2016.
 */

public class SummaryFragment extends Fragment {

    private TextView mCurrentUserView;
    private TextView mCurrentServiceView;
    private Button mActionEventsView;
    private Button mActionTEsView;
    private Button mActionTimetablesView;
    private Button mActionArticlesView;
    private Button mActionKanaView;
    private Button mActionRakingsView;

    private SoundPool mCenaPool;

    public SummaryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        mCurrentUserView = (TextView) view.findViewById(R.id.summary_current_user);
        User user = Bullshit.dataLoader.getCurrentUser();
        if (user == null) {
            mCurrentUserView.setText(getString(R.string.none_selected_class, Bullshit.dataLoader.getLastSclass().getName()));
        } else {
            mCurrentUserView.setText(getString(R.string.welcome_x, user.getClassMember().getFullName()));
        }

        mCurrentServiceView = (TextView) view.findViewById(R.id.summary_current_service);
        List<ClassMember> currentService = Bullshit.dataLoader.getCurrentServices();
        if (currentService.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ClassMember serviceMember : currentService) {
                stringBuilder.append(serviceMember.getFullName());
                if (currentService.indexOf(serviceMember) != currentService.size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            mCurrentServiceView.setText(stringBuilder.toString());
        } else {
            mCurrentServiceView.setText(R.string.no_service_found);
        }

        final OpenFragmentListener listener = (OpenFragmentListener) getActivity();

        mCenaPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 100);
        final int cenaId = mCenaPool.load(getContext(), R.raw.johncena, 1);

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
        mActionArticlesView = (Button) view.findViewById(R.id.summary_action_articles);
        mActionArticlesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_articles);
            }
        });
        mActionKanaView = (Button) view.findViewById(R.id.summary_action_kana);
        mActionKanaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int priority = 1;
                int noLoop = 0;
                Random random = new Random();
                float normalPlaybackRate = 0.7f + random.nextFloat() * 1.3f;
                mCenaPool.play(cenaId, 1, 1, priority, noLoop, normalPlaybackRate);
            }
        });
        mActionRakingsView = (Button) view.findViewById(R.id.summary_action_rakings);
        mActionRakingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_rakings);
            }
        });

        return view;
    }

    public interface OpenFragmentListener {
        void setSelectedFragmentAndOpen(int id);
    }
}
