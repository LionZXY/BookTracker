package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.user.IUser;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class SetPermission extends ICommand {
    public SetPermission() {
        super("!нп");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        if (user.getTypeID() == 76844299)
            BookTracker.DB.updateUser(MainParser.getUserByVkId(Integer.parseInt(arg.substring(0, arg.indexOf(" ")))).setPerm(Integer.parseInt(arg.substring(arg.lastIndexOf(" ") + 1))));
    }
}
