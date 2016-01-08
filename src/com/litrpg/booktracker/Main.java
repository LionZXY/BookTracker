package com.litrpg.booktracker;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.event.SubscribeMessageEvent;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.message.MessageListiner;

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
        MessageListiner msg = new MessageListiner();
    }
}
