package com.farast.utu_apibased.fragments.te;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.util.DateOffseter;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.custom_views.UtuLineGenericViewHolder;
import com.farast.utu_apibased.listeners.OnListFragmentInteractionListener;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Lesson;
import com.farast.utuapi.data.SchoolDay;
import com.farast.utuapi.data.Timetable;
import com.farast.utuapi.data.interfaces.TEItem;
import com.farast.utuapi.util.CollectionUtil;
import com.farast.utuapi.util.functional_interfaces.Predicate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class TEsAdapter extends RecyclerView.Adapter<UtuLineGenericViewHolder> {
    private List<TEItem> mValues;
    private OnListFragmentInteractionListener<TEItem> mListener;
    private List<Lesson> mAllAvailableLessons;
    private DataLoader.OnDataSetListener mDataSetListener;

    public TEsAdapter(OnListFragmentInteractionListener<TEItem> listener, Context context) {
        mValues = Bullshit.dataLoader.getTEsList();
        mListener = listener;

        List<Timetable> allTimetables = Bullshit.dataLoader.getTimetablesList();
        Timetable mBestTimetable;
        if (Bullshit.dataLoader.getCurrentUser() != null) {
            mBestTimetable = Timetable.getBestTimetableForClassMember(
                    allTimetables,
                    Bullshit.dataLoader.getCurrentUser().getClassMember());
        } else {
            if (allTimetables.size() > 0) {
                mBestTimetable = allTimetables.get(0);
            } else {
                throw new RuntimeException("No timetables downloaded");
            }
        }
        mAllAvailableLessons = new ArrayList<>();
        if (mBestTimetable != null) {
            for (SchoolDay day : mBestTimetable.getSchoolDays()) {
                mAllAvailableLessons.addAll(day.getLessons());
            }
        }

        final Handler handler = new Handler(context.getMainLooper());

        mDataSetListener = new DataLoader.OnDataSetListener() {
            @Override
            public void onDataSetChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mValues.clear();
                        mValues.addAll(Bullshit.dataLoader.getTEsList());
                        notifyDataSetChanged();
                    }
                });
            }
        };
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        Bullshit.dataLoader.getNotifier().addListener(DataLoader.EventType.TASKS, mDataSetListener);
        Bullshit.dataLoader.getNotifier().addListener(DataLoader.EventType.EXAMS, mDataSetListener);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        Bullshit.dataLoader.getNotifier().removeListener(DataLoader.EventType.TASKS, mDataSetListener);
        Bullshit.dataLoader.getNotifier().removeListener(DataLoader.EventType.EXAMS, mDataSetListener);
    }

    @Override
    public UtuLineGenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.utu_line_generic, parent, false);
        return new UtuLineGenericViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UtuLineGenericViewHolder holder, int position) {
        final TEItem item = mValues.get(position);
        holder.mTitleView.setText(item.getTitle());
        holder.mAvatarTextView.setText(item.getSubject().getName());

        List<Lesson> goodLessons = CollectionUtil.filter(item.getLessons(), new Predicate<Lesson>() {
            @Override
            public boolean test(Lesson lesson) {
                return item.getLessons().contains(lesson);
            }
        });
        Date dateToSet;
        if (goodLessons.size() > 0) {
            dateToSet = goodLessons.get(0).getLessonTiming().getStart().getOffsetDate(item.getDate());
        } else {
            dateToSet = DateOffseter.addRegularDateOffset(item.getDate());
        }
        holder.mLeftBottomView.setText(Bullshit.prettyDate(dateToSet));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}
