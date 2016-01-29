package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.lionzxy.vkapi.users.User;
import com.litrpg.booktracker.user.IUser;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class Ping extends ICommand {
    public Ping() {
        super("!пинг");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        MessageBuffer.addMessage(new Message("Я тебе не какой-то бездушный робот, чтобы отзываться на твои глупые провокации!").addMedia("photo286477373_399671795"), new User(user.getTypeID()));
    }
}
