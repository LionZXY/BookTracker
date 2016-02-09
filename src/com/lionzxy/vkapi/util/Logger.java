package com.lionzxy.vkapi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by LionZXY on 01.01.2016.
 * LeaveBot
 */
public class Logger {
    String name;
    FileOutputStream log = null;
    private static Logger logger = null;

    public Logger(String name) {
        this.name = name;
        File logFile = new File(System.getProperty("user.dir") + "/log/", name + ".log");
        try {
            byte[] buf = null;
            int av = 0;
            if (!logFile.exists()) {
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
            } else {
                FileInputStream fis = new FileInputStream(logFile);
                buf = new byte[fis.available()];
                av = fis.read(buf);
                fis.close();
            }
            this.log = new FileOutputStream(logFile);
            if (buf != null)
                this.log.write(buf, 0, av);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка инициализации системы логирования");
        }
    }

    public void print(String log) {
        if (log != null)
            log = log.replaceAll("\n", '\n' + name + ' ');
        System.out.print(name + ' ' + log + '\n');
        if (this.log != null)
            try {
                this.log.write(('[' + Calendar.getInstance().getTime().toString() + "]" + name + ' ' + log + '\n').getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static Logger getLogger() {
        if (logger == null) {
            return new Logger("[UNNAMED]");
        } else return logger;
    }

    public static Logger setDefaultLogger(String name) {
        logger = new Logger(name);
        return logger;
    }

}
