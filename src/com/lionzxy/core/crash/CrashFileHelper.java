package com.lionzxy.core.crash;

import com.lionzxy.core.io.MultiWritter;
import com.lionzxy.core.io.PrintErrWritter;
import com.lionzxy.vkapi.messages.Message;
import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarDate;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            List<Writer> writers = new ArrayList<>();
            writers.add(new FileWriter(crashFile));
            writers.add(new PrintErrWritter());
            PrintWriter pw = new PrintWriter(new MultiWritter(writers));
            pw.print("---- Crash Report ----\n\n\n");
            pw.print("Time: \n" + new Date() +
                    "\nDescription: " + e.getLocalizedMessage() + "\n\n");
            e.printStackTrace(pw);
            e.printStackTrace();
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public CrashFileHelper(Exception e, Message msg) {
        try {
            File crashFile = new File(System.getProperty("user.dir") + "/crash/" + dateToString(new Date()) + "-crash.log");
            System.out.println(crashFile.getPath());
            crashFile.getParentFile().mkdirs();
            crashFile.createNewFile();
            List<Writer> writers = new ArrayList<>();
            writers.add(new FileWriter(crashFile));
            writers.add(new PrintErrWritter());
            PrintWriter pw = new PrintWriter(new MultiWritter(writers));
            pw.print("---- Crash Report ----\n\n\n");
            pw.print("Time: \n" + new Date() + "\nOn message");
            if (msg.getUser() != null)
                pw.print(" from " + msg.getUser().getId());
            else if (msg.getToUser() != null)
                pw.print(" to " + msg.getToUser().getId());
            pw.print(":\n" + msg.toString() +
                    "\n\nDescription: " + e.getLocalizedMessage() + "\n\n");
            e.printStackTrace(pw);
            e.printStackTrace();
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
