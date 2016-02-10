package com.litrpg.booktracker.message.botanswer.commands;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.lionzxy.vkapi.users.User;
import com.litrpg.booktracker.enums.TypeSite;
import com.litrpg.booktracker.helper.URLHelper;
import com.litrpg.booktracker.message.botanswer.UserBot;
import com.litrpg.booktracker.message.messages.Error;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.user.IUser;

import java.io.FileNotFoundException;

/**
 * com.litrpg.booktracker.message.botanswer.commands
 * Created by LionZXY on 29.01.2016.
 * BookTracker
 */
public class AddBook extends ICommand {
    public AddBook() {
        super("!добавитьКнигу");
    }

    @Override
    public void onMessage(IUser user, VKUser vkUser, Message message, String arg) {
        onMessageS(user, vkUser, message, arg);
    }

    public static void onMessageS(IUser user, VKUser vkUser, Message message, String arg) {
        arg = arg.replaceAll("http://budclub.ru/", "http://samlib.ru/").replaceAll("http://zhurnal.lib.ru/", "http://samlib.ru/");
        Message errMsg = URLHelper.isValidLinkForBook(arg);
        if (errMsg != null) {
            MessageBuffer.addMessage(errMsg, user);
            return;
        } else if (UserBot.isGroupFollower(user))
                switch (TypeSite.getTypeFromUrl(arg)) {
                    case SAMLIB:
                    case LITERA: {
                        try {
                            try {
                                user.addSub(MainParser.getBook(arg));
                                MessageBuffer.addMessage(new Message("Книга \"" + MainParser.getBook(arg).getNameBook() + "\" успешно добавлена к Вам в обновления. Как только она обновится, мы дадим Вам знать!").addMedia("photo286477373_399676422"), new User(user.getTypeID()));
                            } catch (FileNotFoundException ex) {
                                MessageBuffer.addMessage(Error.error404, user);
                            }
                        } catch (Exception e) {
                            new CrashFileHelper(e);
                            MessageBuffer.addMessage(Error.errLink, user);
                        }
                        return;
                    }
                    default:
                        MessageBuffer.addMessage(new Message("Сейчас поддерживаются только книги с Lit-Era и с SamLib"), user);
                }
        else MessageBuffer.addMessage(Error.notInGroup, user);
    }
}
