package com.farast.utu_apibased.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.farast.utu_apibased.Bullshit;
import com.farast.utu_apibased.R;
import com.farast.utu_apibased.activities.MainActivity;
import com.farast.utu_apibased.tasks.DataDownloadTask;
import com.farast.utu_apibased.tasks.PredataDownloadTask;
import com.farast.utuapi.data.Lesson;
import com.farast.utuapi.data.LessonTiming;
import com.farast.utuapi.data.SchoolDay;
import com.farast.utuapi.data.Timetable;
import com.farast.utuapi.data.common.AbsoluteTime;
import com.farast.utuapi.util.CollectionUtil;
import com.farast.utuapi.util.functional_interfaces.Predicate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cendr on 13/02/2017.
 */
public class NotificationReceiver extends BroadcastReceiver {

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        new NotificationPredataDownloadTask().execute();
    }

    private void scheduleNotification(long milisecondsDiff) {
        Intent notificationIntent = new Intent(mContext, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + milisecondsDiff;
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, futureInMillis, pendingIntent);
    }


    private class NotificationPredataDownloadTask extends PredataDownloadTask {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                new NotificationDataDownloadTask().execute(new DataDownloadTask.Params(Bullshit.dataLoader.getLastSclass().getId()));
            }
        }
    }

    private class NotificationDataDownloadTask extends DataDownloadTask {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Timetable sourceTimetable;
                if (Bullshit.dataLoader.getCurrentUser() != null) {
                    sourceTimetable = Timetable.getBestTimetableForClassMember(Bullshit.dataLoader.getTimetablesList(), Bullshit.dataLoader.getCurrentUser().getClassMember());
                } else {
                    sourceTimetable = Bullshit.dataLoader.getTimetablesList().get(1); // temporarily use the second timetable
                }

                Lesson sourceLesson = null;
                for (SchoolDay schoolDay : sourceTimetable.getSchoolDays()) {
                    for (Lesson lesson : schoolDay.getLessons()) {
                        Date lessonStart = lesson.getLessonTiming().getStart().getOffsetDate(schoolDay.getDate());
                        Date lessonEnd = lesson.getLessonTiming().getDuration().getOffsetDate(lessonStart);
                        Date startOfBreakBeforeLesson = new AbsoluteTime(0, -5, 0).getOffsetDate(lessonStart);
                        Date now = new Date();
                        if (now.after(startOfBreakBeforeLesson) && now.before(lessonEnd)) {
                            sourceLesson = lesson;
                        }
                    }
                }

                if (sourceLesson != null) {
                    // send one notification
                    Intent viewIntent = new Intent(mContext, MainActivity.class);
                    viewIntent.putExtra("sclass_id", Bullshit.dataLoader.getLastSclass().getId());
                    PendingIntent viewPendingIntent =
                            PendingIntent.getActivity(mContext, 0, viewIntent, 0);

                    if (!sourceLesson.isNotNormal()) {
                        NotificationCompat.Builder notificationBuilder =
                                new NotificationCompat.Builder(mContext)
                                        .setSmallIcon(R.drawable.ic_event)
                                        .setContentTitle(sourceLesson.getSubject().getName() + " " + sourceLesson.getRoom())
                                        .setContentText(sourceLesson.getLessonTiming().getStart().getHoursMinutesString())
                                        .setContentIntent(viewPendingIntent);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
                        notificationManager.notify(0, notificationBuilder.build());
                    }

                    // schedule new notification
                    final Lesson finalSourceLesson = sourceLesson;
                    List<LessonTiming> selectedTimings = CollectionUtil.filter(Bullshit.dataLoader.getLessonTimingsList(), new Predicate<LessonTiming>() {
                        @Override
                        public boolean test(LessonTiming lessonTiming) {
                            return lessonTiming.getSerialNumber() == finalSourceLesson.getLessonTiming().getSerialNumber() + 1;
                        }
                    });
                    if (selectedTimings.size() > 0) {
                        Calendar calendar = Calendar.getInstance();
                        AbsoluteTime currentAbsoluteTime = new AbsoluteTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
                        long milisecondsDiff = (AbsoluteTime.add(
                                sourceLesson.getLessonTiming().getStart(),
                                sourceLesson.getLessonTiming().getDuration())
                                .getTotalSeconds() - currentAbsoluteTime.getTotalSeconds()) * 1000;
                        scheduleNotification(milisecondsDiff);
                    }
                }
            }
        }
    }
}
