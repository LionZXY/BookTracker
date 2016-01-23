package com.lionzxy.core.crash;

import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarDate;

import java.io.*;
import java.util.Date;

/**
 * com.lionzxy.core.crash
 * Created by LionZXY on 23.01.2016.
 * BookTracker
 */
public class CrashFileHelper {

    public CrashFileHelper(Exception e) {
        try {
            File crashFile = new File("/crash/" + dateToString(new Date()) + "-crash.log");
            if (crashFile.createNewFile()) {
                crashFile.getParentFile().mkdirs();
                crashFile.createNewFile();
            }
            PrintWriter pw = new PrintWriter(new FileWriter(crashFile));
            e.printStackTrace(pw);
        } catch (Exception ex) {
            e.printStackTrace();
        }
    }

    public static String dateToString(Date date) {
        //YYYY-MM-DD HH:MM:SS
        CalendarDate calendarDate = BaseCalendar.getGregorianCalendar().getCalendarDate(date.getTime());
        String dateStr = calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDayOfMonth();
        dateStr += "_" + calendarDate.getHours() + ":" + calendarDate.getMinutes() + ":" + calendarDate.getSeconds();
        return dateStr;
    }
}
