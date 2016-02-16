package com.lionzxy.vkapi.messages;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.exceptions.VKException;
import com.lionzxy.vkapi.users.User;
import com.lionzxy.vkapi.util.Logger;
import com.litrpg.booktracker.user.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * com.litrpg.booktracker.message
 * Created by LionZXY on 24.01.2016.
 * BookTracker
 */
public class MessageBuffer implements Runnable {
    private static List<Message> messages = new ArrayList<>();
    public VKUser vkUser;

    public MessageBuffer(VKUser vkUser) {
        this.vkUser = vkUser;
        new Thread(this).start();
    }

    public static void addMessage(Message msg, User user) {
        for (Message message : messages) {
            if (user != null && message.getToUser().getId() == user.getId() && message.getColMedia() + msg.getColMedia() <= 10) {
                message.addMessage(msg);
                return;
            }
        }
        if (user == null)
            messages.add(msg);
        else
            messages.add(msg.setToUser(user));
    }

    public static void addMessage(Message msg, IUser user) {
        addMessage(msg, new User(user.getTypeID()));
    }

    public static void addMessage(String msg, IUser user) {
        addMessage(new Message(msg), user);
    }

    public static void flush(VKUser vkUser) {
        new MessageBuffer(vkUser);
    }

    @Override
    public void run() {
        if (messages.size() != 0)
            VKUser.log.print("Отправка " + messages.size() + " сообщений");
        List<Message> tmpList = new ArrayList<>();
        //Не используется ForEach из-за гонки потоков
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            try {
                try {
                    message.sendMessage(vkUser, message.getToUser());
                } catch (VKException vke) {
                    if (vke.getErrorCode() == 9 || vke.isFix)
                        tmpList.add(message);
                    else {
                        new CrashFileHelper(vke, message);
                    }
                }
            } catch (Exception e) {
                new CrashFileHelper(e, message);
            }
        }
        VKUser.log.print("Не удалось отправить из-за флуда " + tmpList.size() + " сообщений.");
        messages = new ArrayList<>();
        for (Message m : tmpList)
            addMessage(m, (User) null);
    }
}
