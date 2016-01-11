package com.lionzxy.vkapi.util;

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

    public static LinkedHashMap<String,String> getHashMap(String key, String set){
        LinkedHashMap<String,String> toExit = new LinkedHashMap<>();
        toExit.put(key,set);
        return toExit;
    }
}
