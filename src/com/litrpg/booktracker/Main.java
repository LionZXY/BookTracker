package com.litrpg.booktracker;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.vkapi.util.UsersFile;
import com.litrpg.booktracker.message.MessageListiner;
import com.litrpg.booktracker.mysql.MySql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        MessageListiner.sme.checkMessage(new VKUser(UsersFile.getUsers("LeaveBot.usrs")[0]));
        //System.out.println(new VKUser("nikita@kulikof.ru 789456za").inChat(69));
    }
}
