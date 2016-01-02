package com.lionzxy.vkapi.util;

import java.io.*;
import java.util.Calendar;

/**
 * Created by LionZXY on 01.01.2016.
 * LeaveBot
 */
public class Logger {
    String name;
    FileOutputStream log = null;

    public Logger(String name) {
        this.name = name;
        File logFile = new File(name + ".log");
        try {
            byte[] buf = null;
            int av = 0;
            if (!logFile.exists())
                logFile.createNewFile();
            else {
                FileInputStream fis = new FileInputStream(logFile);
                buf = new byte[fis.available()];
                av = fis.read(buf);
            }
            this.log = new FileOutputStream(logFile);
            if (buf != null)
                this.log.write(buf,0,av);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка инициализации системы логирования");
        }
    }

    public void print(String log) {
        System.out.print(name + ' ' + log + '\n');
        if (this.log != null)
            try {
                this.log.write(('[' + Calendar.getInstance().getTime().toString() + "]" + name + ' ' + log + '\n').getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}