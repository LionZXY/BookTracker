package com.lionzxy.core.string;

import java.util.ArrayList;
import java.util.List;

/**
 * com.lionzxy.core.string
 * Created by LionZXY on 31.01.2016.
 * BookTracker
 */
public class Split {

    public static String[] spilitToByte(String str, byte b) {
        List<String> toExit = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
            if ((byte) str.charAt(i) == b) {
                toExit.add(sb.toString());
                sb = new StringBuilder();
            } else sb.append(str.charAt(i));
        toExit.add(sb.toString());
        return toExit.toArray(new String[toExit.size()]);
    }
}
