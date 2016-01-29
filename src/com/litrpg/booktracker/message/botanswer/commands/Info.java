package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.litrpg.booktracker.message.messages.Command;
import com.litrpg.booktracker.user.IUser;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class Info extends ICommand {
    public Info() {
        super("!инфо");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        MessageBuffer.addMessage(Command.info, user);
    }
}
