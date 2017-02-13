package com.farast.utu_apibased.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by cendr on 10/02/2017.
 */

public class DateOffseter {
    public static Date addRegularDateOffset(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 8);
        return calendar.getTime();
    }
}
