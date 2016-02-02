package com.lionzxy.vkapi.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
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
            Logger.getLogger().print("Файл создан по адресу " + file.getPath());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger().print("Не удалось создать файл " + path);
        }
        return null;
    }

    public static File getLinkAsFile(String url) {
        File outputFile = new File("tmpFile" + url.substring(url.lastIndexOf(".")));
        URL urlObj;
        InputStream is = null;
        BufferedReader br;
        String line;
        try {
            outputFile.createNewFile();
            urlObj = new URL(url);
            is = urlObj.openStream();  // throws an IOExceptio

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            DataInputStream inputStream = new DataInputStream(is);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {

            }
        }
        return outputFile;
    }
}
