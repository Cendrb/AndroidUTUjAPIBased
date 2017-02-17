package com.farast.utu_apibased;

import com.farast.utuapi.data.DataLoader;
import com.farast.utuapi.util.DateUtil;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

/**
 * Created by cendr_000 on 03.08.2016.
 */

public class Bullshit {
    public static final String TAG = "UTU jAPI based";
    public static final DataLoader dataLoader = new DataLoader("http://utu.herokuapp.com");
    public static final PrettyTime prettyTime = new PrettyTime();

    public static String prettyDate(Date date) {
        return Bullshit.prettyTime.format(date) + " (" + DateUtil.CZ_WEEK_DATE_FORMAT.format(date) + ")";
    }
}
