package com.lionzxy.core.crash;

import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarDate;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

/**
 * com.lionzxy.core.crash
 * Created by LionZXY on 23.01.2016.
 * BookTracker
 */
public class CrashFileHelper {

    public CrashFileHelper(Exception e) {
        try {
            File crashFile = new File(System.getProperty("user.dir") + "/crash/" + dateToString(new Date()) + "-crash.log");
            System.out.println(crashFile.getPath());
            crashFile.getParentFile().mkdirs();
            crashFile.createNewFile();
            PrintWriter pw = new PrintWriter(new FileWriter(crashFile));
            pw.print("---- Crash Report ----\n\n\n");
            pw.print("Time: \n" + new Date() +
                    "\nDescription: " + e.getLocalizedMessage() + "\n\n");
            e.printStackTrace(pw);
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String dateToString(Date date) {
        //YYYY-MM-DD HH:MM:SS
        CalendarDate calendarDate = BaseCalendar.getGregorianCalendar().getCalendarDate(date.getTime());
        String dateStr = calendarDate.getYear() + "-" + calendarDate.getMonth() + "-" + calendarDate.getDayOfMonth();
        dateStr += "_" + calendarDate.getHours() + "." + calendarDate.getMinutes() + "." + calendarDate.getSeconds();
        return dateStr;
    }
}
