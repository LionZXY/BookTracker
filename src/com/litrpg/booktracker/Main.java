package com.litrpg.booktracker;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.event.SubscribeMessageEvent;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.message.MessageListiner;
import com.litrpg.booktracker.mysql.MySql;
import com.sun.javafx.css.converters.PaintConverter;

import java.util.LinkedHashMap;

/**
 * com.litrpg.booktracker
 * Created by LionZXY on 02.01.2016.
 * BookTracker
 */
public class Main {
    static {
        Logger.setDefaultLogger("[BookTracker]");
    }
    public static void main(String... args) {
        LinkedHashMap<String,String> req = new LinkedHashMap<>();
        req.put("name","AAA");
        req.put("auth","1234");
        req.put("idsamlibbooks","123");
        new MySql("book_updater","root","root").addInTable("users",req);
    }
}
