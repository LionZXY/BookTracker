package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.litrpg.booktracker.helper.URLHelper;
import com.litrpg.booktracker.user.IUser;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 09.02.2016.
 * BookTracker
 */
public class Add extends ICommand {
    public Add() {
        super("!добавить");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        if (URLHelper.isAuthor(arg))
            AddAuthor.onMessageS(user, vkUser, message, arg);
        else
            AddBook.onMessageS(user, vkUser, message, arg);
    }
}
