package com.lionzxy.vkapi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * com.lionzxy.vkapi.util
 * Created by LionZXY on 08.01.2016.
 * LeaveBot
 */
public class UsersFile {

    public static String[] getUsers(String path) {
        List<String> usrs = new ArrayList<>();
        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(path));

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.contains(" "))
                    usrs.add(sCurrentLine);
            }
            return usrs.toArray(new String[usrs.size()]);
        } catch (Exception e) {
        }
        return null;
    }

    public static File save(String s, String path) {
        File file = new File(path);
        try {
            file.createNewFile();
            new FileOutputStream(file).write(s.getBytes());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger().print("Не удалось создать файл " + path);
        }
        return null;
    }
}
