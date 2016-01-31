package com.litrpg.booktracker.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * com.litrpg.booktracker.helper
 * Created by LionZXY on 04.01.2016.
 * BookTracker
 */
public class URLHelper {

    public static String getSiteAsString(String url, String encode) {
        System.out.println("Получение файла " + url);
        StringBuilder stringBuilder = new StringBuilder();

        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        URL urlObj;
        InputStream is = null;
        BufferedReader br;
        String line;
        try {
            urlObj = new URL(url);
            is = urlObj.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is, encode));
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
