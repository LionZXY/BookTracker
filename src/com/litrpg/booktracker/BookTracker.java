package com.litrpg.booktracker;

import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.parsers.LitEraParser;
import com.litrpg.booktracker.parsers.other.ToText;

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
        //new LitEraParser("https://lit-era.com/book/prikladnaya-nekromantiya-zapiski-mezhdu-stranic-b3904").parseBook();
        ToText.getText(new LitEraParser("https://lit-era.com/book/parallel-kniga-chetvertaya-bog-b3228").parseBook());
    }
}
