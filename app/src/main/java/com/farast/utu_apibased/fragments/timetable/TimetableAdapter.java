package com.farast.utu_apibased.fragments.timetable;

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
import com.farast.utu_apibased.UnitsUtil;
import com.farast.utuapi.data.Lesson;
import com.farast.utuapi.data.SchoolDay;
import com.farast.utuapi.data.Timetable;
import com.farast.utuapi.util.DateUtil;

import java.util.List;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class TimetableAdapter extends BaseAdapter {

    private static final int gridWidth = 10;

    private List<SchoolDay> schoolDays;
    private final Timetable timetable;
    private final Context context;
    private final LayoutInflater inflater;

    public TimetableAdapter(Timetable timetable, Context context) {
        schoolDays = timetable.getSchoolDays();
        this.timetable = timetable;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 70;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RelativeLayout relativeLayout;
        if (view == null)
            relativeLayout = (RelativeLayout) inflater.inflate(R.layout.timetable_cell, null);
        else
            relativeLayout = (RelativeLayout) view;
        TextView subject = (TextView) relativeLayout.findViewById(R.id.tcell_subject);
        TextView room = (TextView) relativeLayout.findViewById(R.id.tcell_room);
        TextView teacher = (TextView) relativeLayout.findViewById(R.id.tcell_teacher);

        int row = i / gridWidth;
        int col = i % gridWidth;

        SchoolDay day = schoolDays.get(row);
        if (col == 0)
            subject.setText(DateUtil.CZ_WEEK_DATE_FORMAT.format(day.getDate()));
        else {
            boolean found = false;
            List<Lesson> lessons = day.getLessons();
            for (Lesson lesson : lessons) {
                if (lesson.getSerialNumber() == col) {
                    found = true;
                    subject.setText(lesson.getSubject().getName());
                    teacher.setText(lesson.getTeacher().getAbbr());
                    room.setText(lesson.getRoom());
                    relativeLayout.setBackgroundResource(R.drawable.lesson_background);
                    if (lesson.isNotNormal())
                        ((GradientDrawable) relativeLayout.getBackground()).setColor(ContextCompat.getColor(context, R.color.colorNotNormalCell));
                    else
                        ((GradientDrawable) relativeLayout.getBackground()).setColor(ContextCompat.getColor(context, R.color.colorNormalCell));
                    break;
                }
            }
            if (!found) {
                // cell is empty
            }
        }
        relativeLayout.setLayoutParams(new GridView.LayoutParams(UnitsUtil.dpToPx(60), UnitsUtil.dpToPx(70)));
        return relativeLayout;
    }
}
