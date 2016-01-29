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
public class Bot extends ICommand {
    public Bot() {
        super("!бот");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        switch (arg) {
            case "версия":
                MessageBuffer.addMessage(Command.version, user);
                return;

        }
    }
}
