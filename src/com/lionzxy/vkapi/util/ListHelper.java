package com.lionzxy.vkapi.util;

import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.enums.Genres;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * com.lionzxy.vkapi.util
 * Created by LionZXY on 11.01.2016.
 * BookTracker
 */
public class ListHelper {

    public static List<String> getStringList(String... listValue) {
        List<String> toExit = new ArrayList<>();
        for (String value : listValue)
            toExit.add(value);
        return toExit;
    }

    public static List<Integer> parseString(String str) {
        List<Integer> lst = new ArrayList<>();
        for (String numb : str.split(",")) {
            lst.add(Integer.parseInt(numb));
        }
        return lst;
    }

    public static LinkedHashMap<String, String> getHashMap(String key, String set) {
        LinkedHashMap<String, String> toExit = new LinkedHashMap<>();
        toExit.put(key, set);
        return toExit;
    }

    public static String getAsString(int... numb) {
        StringBuilder toExit = new StringBuilder();
        for (int num : numb) {
            if (toExit.length() != 0) toExit.append(",");
            toExit.append(num);
        }
        return toExit.toString();
    }

    public static String getAsStringAuthor(List<Author> authors) {
        StringBuilder toExit = new StringBuilder();
        for (Author author : authors) {
            if (toExit.length() != 0) toExit.append(",");
            toExit.append(author.getInDB());
        }
        return toExit.toString();
    }

    public static String getAsStringAuthorName(List<Author> authors) {
        StringBuilder toExit = new StringBuilder();
        for (Author author : authors) {
            if (toExit.length() != 0) toExit.append(", ");
            toExit.append(author.getName());
        }
        return toExit.toString();
    }

    public static String getAsStringGenr(List<Genres> genres) {
        StringBuilder toExit = new StringBuilder();
        for (Genres author : genres) {
            if (toExit.length() != 0) toExit.append(",");
            toExit.append(author.ordinal());
        }
        return toExit.toString();
    }
}
