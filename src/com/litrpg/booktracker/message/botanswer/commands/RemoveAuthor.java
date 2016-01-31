package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.litrpg.booktracker.authors.Author;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.user.IUser;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class RemoveAuthor extends ICommand {
    public RemoveAuthor() {
        super("!удалитьАвтора");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        if (arg.length() > 0) {
            Author author = MainParser.getAuthor(arg);
            user.removeSub(author);
            MessageBuffer.addMessage(new Message("Автор удален").addMedia("photo286477373_400693046"), user);
        } else MessageBuffer.addMessage("Для этой комманды нужна ссылка", user);

    }
}
