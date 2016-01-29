package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.message.messages.Command;
import com.litrpg.booktracker.user.IUser;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class Stop extends ICommand {
    public Stop() {
        super("!стоп");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        MessageBuffer.addMessage(Command.stop, user);
        BookTracker.stop = true;
    }
}
