package com.farast.utu_apibased.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farast.utu_apibased.R;
import com.farast.utu_apibased.util.UnitsUtil;
import com.farast.utuapi.data.Lesson;
import com.farast.utuapi.data.SchoolDay;
import com.farast.utuapi.data.common.AbsoluteTime;
import com.farast.utuapi.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class TimetableAdapter extends BaseAdapter {

    private static int gridWidth;

    private List<SchoolDay> mSchoolDays;
    private final Context context;
    private final LayoutInflater inflater;
    private final ArrayList<LessonViewData> mGridedLessons;

    public TimetableAdapter(Context context) {
        mGridedLessons = new ArrayList<>();
        mSchoolDays = new ArrayList<>();
        this.context = context;
        inflater = LayoutInflater.from(context);
        gridWidth = 10;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return mGridedLessons.size();
    }

    @Override
    public Object getItem(int i) {
        return mGridedLessons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RelativeLayout relativeLayout;
        if (view == null)
            relativeLayout = (RelativeLayout) inflater.inflate(R.layout.timetable_cell, null);
        else
            relativeLayout = (RelativeLayout) view;

        mGridedLessons.get(i).populate(relativeLayout, context);

        return relativeLayout;
    }

    private void refresh() {
        int maxLessonNumber = 0;
        for (SchoolDay schoolDay : mSchoolDays) {
            for (Lesson lesson : schoolDay.getLessons()) {
                if (maxLessonNumber < lesson.getSerialNumber()) {
                    maxLessonNumber = lesson.getSerialNumber();
                }
            }
        }
        gridWidth = maxLessonNumber + 1; // add one for current day/date

        for (int i = 0; i < gridWidth * mSchoolDays.size(); i++) {
            int row = i / gridWidth;
            int col = i % gridWidth;

            SchoolDay schoolDay = mSchoolDays.get(row);
            LessonViewData lessonViewData = null;
            if (col == 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(schoolDay.getDate());
                lessonViewData = new LessonViewData(DateUtil.CZ_WEEK_DATE_FORMAT.format(schoolDay.getDate()), "", "", LessonCellType.DATE);
            } else {
                for (Lesson lesson : schoolDay.getLessons()) {
                    if (lesson.getSerialNumber() == col) {
                        String title;
                        String subLeft;
                        String subRight;
                        if (!lesson.getEventName().equals("")) {
                            title = lesson.getEventName();
                            subLeft = "";
                            subRight = "";
                        } else {
                            title = lesson.getSubject() != null ? lesson.getSubject().getName() : "";
                            subLeft = lesson.getRoom();
                            subRight = lesson.getTeacher() != null ? lesson.getTeacher().getAbbr() : "";
                        }

                        Date lessonStart = lesson.getLessonTiming().getStart().getOffsetDate(schoolDay.getDate());
                        Date lessonEnd = lesson.getLessonTiming().getDuration().getOffsetDate(lessonStart);
                        Date startOfBreakBeforeLesson = new AbsoluteTime(0, -5, 0).getOffsetDate(lessonStart);
                        Date now = new Date();
                        if (now.after(startOfBreakBeforeLesson) && now.before(lessonEnd)) {
                            lessonViewData = new LessonViewData(title, subRight, subLeft, LessonCellType.RIGHT_NOW);
                        } else if (!lesson.getEventName().equals("")) {
                            lessonViewData = new LessonViewData(title, subRight, subLeft, LessonCellType.EVENT);
                        } else if (lesson.isNotNormal()) {
                            lessonViewData = new LessonViewData(title, subRight, subLeft, LessonCellType.IRREGULAR);
                        } else {
                            lessonViewData = new LessonViewData(title, subRight, subLeft, LessonCellType.NORMAL);
                        }
                    }
                    if (lessonViewData == null) {
                        lessonViewData = new LessonViewData("", "", "", LessonCellType.EMPTY);
                    }
                }
            }

            mGridedLessons.add(i, lessonViewData);
        }

        notifyDataSetInvalidated();
    }

    public void setSchoolDayData(List<SchoolDay> schoolDays) {
        mGridedLessons.clear();
        mSchoolDays.clear();
        mSchoolDays.addAll(schoolDays);

        refresh();
    }

    private static class LessonViewData {
        private String mSubject;
        private String mTeacher;
        private String mRoom;
        private LessonCellType mCellType;

        public LessonViewData(String subject, String teacher, String room, LessonCellType cellType) {
            this.mSubject = subject;
            this.mTeacher = teacher;
            this.mRoom = room;
            this.mCellType = cellType;
        }

        public void populate(RelativeLayout targetView, Context context) {
            TextView subject = (TextView) targetView.findViewById(R.id.tcell_subject);
            TextView room = (TextView) targetView.findViewById(R.id.tcell_room);
            TextView teacher = (TextView) targetView.findViewById(R.id.tcell_teacher);

            subject.setText(mSubject);
            room.setText(mRoom);
            teacher.setText(mTeacher);

            targetView.setBackgroundResource(R.drawable.lesson_background);
            GradientDrawable cellBackground = ((GradientDrawable) targetView.getBackground());

            switch (mCellType) {
                case DATE:
                    cellBackground.setColor(ContextCompat.getColor(context, R.color.cellDateSection));
                    break;
                case NORMAL:
                    cellBackground.setColor(ContextCompat.getColor(context, R.color.cellNormal));
                    break;
                case IRREGULAR:
                    cellBackground.setColor(ContextCompat.getColor(context, R.color.cellNotNormal));
                    break;
                case RIGHT_NOW:
                    cellBackground.setColor(ContextCompat.getColor(context, R.color.cellRightNow));
                    break;
                case EMPTY:
                    cellBackground.setColor(ContextCompat.getColor(context, R.color.cellEmpty));
                    break;
                case EVENT:
                    cellBackground.setColor(ContextCompat.getColor(context, R.color.cellEvent));
                    break;
            }

            targetView.setLayoutParams(new GridView.LayoutParams(UnitsUtil.dpToPx(60), UnitsUtil.dpToPx(70)));
        }
    }

    private enum LessonCellType {DATE, NORMAL, IRREGULAR, EMPTY, RIGHT_NOW, EVENT}
}
