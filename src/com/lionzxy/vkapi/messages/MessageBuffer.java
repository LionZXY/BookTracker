package com.lionzxy.vkapi.messages;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
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
public class MessageBuffer {
    private static List<Message> messages = new ArrayList<>();

    public static void addMessage(Message msg, User user) {
        for (Message message : messages) {
            if (message.getToUser().getId() == user.getId() && message.getColMedia() + msg.getColMedia() <= 10) {
                message.addMessage(msg);
                return;
            }
        }
        messages.add(msg.setToUser(user));
    }

    public static void addMessage(Message msg, IUser user) {
        addMessage(msg, new User(user.getTypeID()));
    }

    public static void flush(VKUser vkUser) {
        VKUser.log.print("Отправка " + messages.size() + " сообщений");
        for (Message message : messages)
            message.sendMessage(vkUser, message.getToUser());
        messages = new ArrayList<>();
    }
}
