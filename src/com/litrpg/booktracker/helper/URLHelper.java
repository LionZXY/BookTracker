package com.litrpg.booktracker.helper;

import com.lionzxy.vkapi.messages.Message;
import com.litrpg.booktracker.exception.PageNotFound;
import com.litrpg.booktracker.message.messages.Error;

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

    public static String getSiteAsString(String url, String encode) throws PageNotFound {
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
                stringBuilder.append(line).append("\n");
            }
            if (br.toString().contains("<TITLE>404 Not Found</TITLE>"))
                throw new PageNotFound();
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

    public static Message isValidLink(String link) {
        if (link == null)
            return new Message("Эта команда требует ссылки!").addMedia("photo286477373_399671795");
        if (link.startsWith("%"))
            return Error.withoutProc;
        if (!isAuthor(link) && !isBook(link))
            return new Message("Неверная ссылка! Требуется указать ссылку на СТРАНИЧКУ книги/автора. Пример правильной ссылки:\n" +
                    "https://lit-era.com/book/losa-deserta-pustynnye-zemli-svadebnye-tancy-b5482\n" +
                    "На данный момент поддерживается только книги с Самиздата и книги с Лит-Эры.").addMedia("photo286477373_399685563");
        return null;
    }

    public static boolean isBook(String link) {
        return link != null && (
                ((link.startsWith("http://samlib") || link.startsWith("https://samlib")) && link.endsWith(".shtml"))
                        || link.startsWith("http://lit-era.com/book") || link.startsWith("https://lit-era.com/book"));
    }

    public static boolean isAuthor(String link) {
        return link != null && (
                ((link.startsWith("http://samlib") || link.startsWith("https://samlib")) && !link.endsWith(".shtml"))
                        || link.startsWith("http://lit-era.com/author") || link.startsWith("https://lit-era.com/author"));
    }
}
