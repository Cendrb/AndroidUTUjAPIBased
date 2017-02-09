package com.farast.utu_apibased.fragments;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.farast.utu_apibased.BindableViewHolder;
import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.ReloadableActivity;
import com.farast.utuapi.data.ClassMember;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.User;

import java.util.List;
import java.util.Random;

/**
 * Created by cendr_000 on 21.08.2016.
 */

public class SummaryFragment extends Fragment implements ReloadableActivity {

    private View mView;
    private ViewHolder mViewHolder;

    private SoundPool mCenaPool;

    public SummaryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_summary, container, false);

        mViewHolder = new ViewHolder();
        mViewHolder.bindViewFields();

        final Handler handler = new Handler(getContext().getMainLooper());

        reloadData();

        Bullshit.dataLoader.getNotifier().setAnyDataListener(new DataLoader.OnDataSetListener() {
            @Override
            public void onDataSetChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        reloadData();
                    }
                });
            }
        });

        final OpenFragmentListener listener = (OpenFragmentListener) getActivity();

        mCenaPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 100);
        final int cenaId = mCenaPool.load(getContext(), R.raw.johncena, 1);


        mViewHolder.mActionEventsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_events);
            }
        });

        mViewHolder.mActionTEsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_tes);
            }
        });

        mViewHolder.mActionTimetablesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_timetables);
            }
        });

        mViewHolder.mActionArticlesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_articles);
            }
        });

        mViewHolder.mActionKanaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int priority = 1;
                int noLoop = 0;
                Random random = new Random();
                float normalPlaybackRate = 0.7f + random.nextFloat() * 1.3f;
                mCenaPool.play(cenaId, 1, 1, priority, noLoop, normalPlaybackRate);
            }
        });

        mViewHolder.mActionRakingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_rakings);
            }
        });

        return mView;
    }

    @Override
    public void reloadData() {
        User user = Bullshit.dataLoader.getCurrentUser();
        if (user == null) {
            mViewHolder.mCurrentUserView.setText(getString(R.string.none_selected_class, Bullshit.dataLoader.getLastSclass().getName()));
        } else {
            mViewHolder.mCurrentUserView.setText(getString(R.string.welcome_x, user.getClassMember().getFullName()));
        }


        List<ClassMember> currentService = Bullshit.dataLoader.getCurrentServices();
        if (currentService.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ClassMember serviceMember : currentService) {
                stringBuilder.append(serviceMember.getFullName());
                if (currentService.indexOf(serviceMember) != currentService.size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            mViewHolder.mCurrentServiceView.setText(stringBuilder.toString());
        } else {
            mViewHolder.mCurrentServiceView.setText(R.string.no_service_found);
        }
    }

    public interface OpenFragmentListener {
        void setSelectedFragmentAndOpen(int id);
    }

    private class ViewHolder implements BindableViewHolder {
        private TextView mCurrentUserView;
        private TextView mCurrentServiceView;
        private Button mActionEventsView;
        private Button mActionTEsView;
        private Button mActionTimetablesView;
        private Button mActionArticlesView;
        private Button mActionKanaView;
        private Button mActionRakingsView;

        @Override
        public void bindViewFields() {
            mCurrentUserView = (TextView) mView.findViewById(R.id.summary_current_user);
            mCurrentServiceView = (TextView) mView.findViewById(R.id.summary_current_service);
            mActionEventsView = (Button) mView.findViewById(R.id.summary_action_events);
            mActionTEsView = (Button) mView.findViewById(R.id.summary_action_tes);
            mActionTimetablesView = (Button) mView.findViewById(R.id.summary_action_timetable);
            mActionArticlesView = (Button) mView.findViewById(R.id.summary_action_articles);
            mActionKanaView = (Button) mView.findViewById(R.id.summary_action_kana);
            mActionRakingsView = (Button) mView.findViewById(R.id.summary_action_rakings);
        }
    }
}
