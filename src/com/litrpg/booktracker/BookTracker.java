package com.litrpg.booktracker;

import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.parsers.LitEraParser;

/**
 * com.litrpg.booktracker
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class BookTracker {
    static {
        Logger.setDefaultLogger("[BookTracker]");
    }

    public static void main(String... args) {
        LitEraParser.parseBook(null);
    }
}
