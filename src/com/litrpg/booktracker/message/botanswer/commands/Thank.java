package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.lionzxy.vkapi.users.User;
import com.litrpg.booktracker.message.messages.Command;
import com.litrpg.booktracker.user.IUser;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class Thank extends ICommand {
    public Thank() {
        super("!сказатьСпасибо");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        if (arg.length() > 0)
            MessageBuffer.addMessage(new Message("Сообщение от " + user.getTypeID() + ":\n" + arg), new User(76844299));
        MessageBuffer.addMessage(Command.money, user);
    }
}
