package com.lionzxy.vkapi.util;

import java.io.BufferedReader;
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
}