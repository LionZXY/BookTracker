package com.litrpg.booktracker.message.botanswer;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.lionzxy.vkapi.users.User;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.message.botanswer.commands.ICommand;
import com.litrpg.booktracker.message.messages.Error;
import com.litrpg.booktracker.parsers.MainParser;
import com.litrpg.booktracker.user.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * com.litrpg.booktracker.message.botanswer
 * Created by LionZXY on 22.01.2016.
 * BookTracker
 */
public class UserBot implements IAnswer {

    public static List<Integer> fantasySubscr = new ArrayList<>();
    public static List<Integer> litrpgSubscr = new ArrayList<>();

    @Override
    public void onMessage(Message msg, VKUser vkUser) {
        if (msg.toString().startsWith("!")) {
            Logger.getLogger().print("Обработка сообщения " + msg);
            IUser user = MainParser.getUserByVkId(msg.getUser().getId());
            if (user == null)
                user = MainParser.addUser(new com.litrpg.booktracker.user.VKUser(msg.getUser()));
            try {
                if (!ICommand.onMessage(user, vkUser, msg))
                    MessageBuffer.addMessage(new Message("Хм... Команды \"" + msg.toString() + "\" у бота нет. Ты уверен, что всё правильно ввел? Попробуй посмотреть доступные комманды \"!инфо\" ").addMedia("photo286477373_399674456"), new User(user.getTypeID()));
            } catch (Exception e) {
                new CrashFileHelper(e, msg);
                Logger.getLogger().print("Ошибка при обработке комманды " + msg.toString());
                MessageBuffer.addMessage(Error.errorMsg, user);
            }
        }
    }

    public static boolean isGroupFollower(IUser user) {
        return litrpgSubscr.contains(user.getTypeID()) || fantasySubscr.contains(user.getTypeID());
    }


}
