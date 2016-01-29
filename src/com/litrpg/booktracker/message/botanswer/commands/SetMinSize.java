package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.litrpg.booktracker.BookTracker;
import com.litrpg.booktracker.user.IUser;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class SetMinSize extends ICommand {
    public SetMinSize() {
        super("!минимальныйРазмер");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        int oldSize = user.getSizeUpdate();
        user.setSizeUpdate(Integer.parseInt(arg));
        BookTracker.DB.updateUser(user);
        MessageBuffer.addMessage(new Message("Теперь оповещения будут приходить вам, только если размер обновления больше " + user.getSizeUpdate() + " символов. Старый минимальный порог обновления " + oldSize + " символов."), user);

    }
}
