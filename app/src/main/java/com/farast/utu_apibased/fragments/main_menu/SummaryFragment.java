package com.farast.utu_apibased.fragments.main_menu;

import android.content.Context;
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

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.activities.BindableViewHolder;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Service;
import com.farast.utuapi.data.User;

import java.util.Random;

/**
 * Created by cendr_000 on 21.08.2016.
 */

public class SummaryFragment extends Fragment implements DataLoader.OnDataSetListener {

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

        onDataSetChanged();

        final OpenFragmentListener listener = (OpenFragmentListener) getActivity();

        mCenaPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 100);
        final int cenaId = mCenaPool.load(getContext(), R.raw.johncena, 1);


        mViewHolder.mNavEventsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_events);
            }
        });

        mViewHolder.mNavTasksView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_tasks);
            }
        });

        mViewHolder.mNavExamsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_exams);
            }
        });

        mViewHolder.mNavTimetablesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_timetables);
            }
        });

        mViewHolder.mNavArticlesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_articles);
            }
        });

        mViewHolder.mNavKanaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int priority = 1;
                int noLoop = 0;
                Random random = new Random();
                float normalPlaybackRate = 0.7f + random.nextFloat() * 1.3f;
                mCenaPool.play(cenaId, 1, 1, priority, noLoop, normalPlaybackRate);
            }
        });

        mViewHolder.mNavRakingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_rakings);
            }
        });

        mViewHolder.mNavServicesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setSelectedFragmentAndOpen(R.id.nav_services);
            }
        });

        mViewHolder.mNavTempView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent notificationIntent = new Intent(getContext(), NotificationReceiver.class);
                //getContext().sendBroadcast(notificationIntent);
            }
        });

        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bullshit.dataLoader.getNotifier().addListener(DataLoader.EventType.LOAD_FINISHED, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Bullshit.dataLoader.getNotifier().removeListener(DataLoader.EventType.LOAD_FINISHED, this);
    }

    @Override
    public void onDataSetChanged() {
        final Handler handler = new Handler(getActivity().getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                User user = Bullshit.dataLoader.getCurrentUser();
                if (user == null) {
                    mViewHolder.mCurrentUserView.setText(getString(R.string.none_selected_class, Bullshit.dataLoader.getLastSclass().getName()));
                } else {
                    mViewHolder.mCurrentUserView.setText(getString(R.string.welcome_x, user.getClassMember().getFullName()));
                }

                Service service = Bullshit.dataLoader.getCurrentService();
                if (!Bullshit.dataLoader.isLoaded()) {
                    mViewHolder.mCurrentServiceView.setText(R.string.operation_generic_loading);
                } else if (service != null) {
                    mViewHolder.mCurrentServiceView.setText(service.getFirstMember().getFullName() + " " + getString(R.string.and) + " " + service.getSecondMember().getFullName());
                } else {
                    mViewHolder.mCurrentServiceView.setText(R.string.no_service_found);
                }
            }
        });
    }

    public interface OpenFragmentListener {
        void setSelectedFragmentAndOpen(int id);
    }

    private class ViewHolder implements BindableViewHolder {
        private TextView mCurrentUserView;
        private TextView mCurrentServiceView;
        private Button mNavEventsView;
        private Button mNavTasksView;
        private Button mNavExamsView;
        private Button mNavTimetablesView;
        private Button mNavArticlesView;
        private Button mNavKanaView;
        private Button mNavRakingsView;
        private Button mNavServicesView;
        private Button mNavTempView;

        @Override
        public void bindViewFields() {
            mCurrentUserView = (TextView) mView.findViewById(R.id.summary_current_user);
            mCurrentServiceView = (TextView) mView.findViewById(R.id.summary_current_service);
            mNavEventsView = (Button) mView.findViewById(R.id.summary_action_events);
            mNavTasksView = (Button) mView.findViewById(R.id.summary_action_tasks);
            mNavExamsView = (Button) mView.findViewById(R.id.summary_action_exams);
            mNavTimetablesView = (Button) mView.findViewById(R.id.summary_action_timetables);
            mNavArticlesView = (Button) mView.findViewById(R.id.summary_action_articles);
            mNavKanaView = (Button) mView.findViewById(R.id.summary_action_kana);
            mNavRakingsView = (Button) mView.findViewById(R.id.summary_action_rakings);
            mNavServicesView = (Button) mView.findViewById(R.id.summary_action_services);
            mNavTempView = (Button) mView.findViewById(R.id.summary_action_nothing1);
        }
    }
}
