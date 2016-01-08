package com.lionzxy.vkapi.util;

import java.util.ArrayList;

/**
 * com.lionzxy.vkapi.util
 * Created by LionZXY on 08.01.2016.
 * BookTracker
 */
public class MarketArrayList<E> extends ArrayList<E> {
    private int mark = 0;

    public void mark() {
        mark++;
    }

    public int getMarked() {
        return mark;
    }

    public int getUnMark() {
        return super.size() - getMarked();
    }
}
