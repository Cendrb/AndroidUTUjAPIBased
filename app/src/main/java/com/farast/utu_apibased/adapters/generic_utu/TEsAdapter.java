package com.farast.utu_apibased.adapters.generic_utu;

import android.content.Context;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.custom_views.UtuLineGenericViewHolder;
import com.farast.utu_apibased.util.DateOffseter;
import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.data.Lesson;
import com.farast.utuapi.data.SchoolDay;
import com.farast.utuapi.data.Timetable;
import com.farast.utuapi.data.interfaces.TEItem;
import com.farast.utuapi.util.CollectionUtil;
import com.farast.utuapi.util.functional_interfaces.Function;
import com.farast.utuapi.util.functional_interfaces.Predicate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cendr_000 on 05.08.2016.
 */

public class TEsAdapter<TItemType extends TEItem> extends GenericUtuAdapter<TItemType> {
    private List<Lesson> mAllAvailableLessons;

    public TEsAdapter(Context context, Function<List<TItemType>> itemsGetter, List<DataLoader.EventType> eventsToListenFor) {
        super(context, itemsGetter, eventsToListenFor);

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
    }

    @Override
    protected final void bindViewHolderToItem(UtuLineGenericViewHolder viewHolder, final TItemType item) {
        viewHolder.mTitleView.setText(item.getTitle());
        viewHolder.mAvatarTextView.setText(item.getSubject().getName());

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
        viewHolder.mLeftBottomView.setText(Bullshit.prettyDate(dateToSet));
    }
}
