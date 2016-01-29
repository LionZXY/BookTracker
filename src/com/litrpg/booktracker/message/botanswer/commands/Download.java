package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.documents.VkFile;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.litrpg.booktracker.message.messages.Command;
import com.litrpg.booktracker.message.messages.Error;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.parsers.other.ToText;
import com.litrpg.booktracker.user.IUser;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class Download extends ICommand {
    public Download() {
        super("!скачать");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        if (user.getPerm() >= 50)
            MessageBuffer.addMessage(Command.download
                    .addMedia(new VkFile(ToText.getAsFile(MainParser.getBook(arg)), vkUser).getAsVkMedia()), user);
        else
            MessageBuffer.addMessage(Error.permErr, user);
    }
}
