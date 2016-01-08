package com.lionzxy.vkapi.messages;

import com.lionzxy.vkapi.exceptions.VKException;
import com.lionzxy.vkapi.users.User;
import com.lionzxy.vkapi.VKUser;
import org.json.simple.JSONObject;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by LionZXY on 30.12.2015.
 * LeaveBot
 */
public class Message {
    String message = null;
    StringBuilder media = new StringBuilder();
    User user = null;
    Date date = null;
    boolean read_state, isOut;
    int idMess;

    public Message(String message) {
        this.message = message;
    }

    public Message(JSONObject msg) {
        idMess = Integer.parseInt(msg.get("mid").toString());
        user = new User(Integer.parseInt(msg.get("uid").toString()));
        date = new Date(Long.parseLong(msg.get("date").toString()));
        read_state = Byte.parseByte(msg.get("read_state").toString()) == 1;
        isOut = Byte.parseByte(msg.get("out").toString()) == 1;
        message = msg.get("body").toString();
        media.append(msg.get("attachments"));
    }

    public Message setUser(User user) {
        this.user = user;
        return this;
    }

    public Message setDate(long date) {
        this.date = new Date(date);
        return this;
    }

    public Message addMedia(String media) {
        if (this.media.length() > 0)
            this.media.append(',');
        this.media.append(media);
        return this;
    }


    public boolean sendMessage(VKUser from, User to) {

        if (!to.isFriend(from)) {
            if (from.messageSendNotFriend > 19) {
                from.preLogin();
            } else from.messageSendNotFriend++;
        }

        if (message.length() > 4090) {
            if (!to.isFriend(from) && 20 - from.messageSendNotFriend < message.length() / 4090)
                from.preLogin();
            VKUser.log.print("Send big message!" + message.length() + " Symbols");
            String newMessage = null;
            if (message.lastIndexOf(".", 4090) != -1)
                newMessage = message.substring(0, message.lastIndexOf(".", 4090) + 1);
            else if (message.lastIndexOf(" ", 4090) != -1)
                newMessage = message.substring(0, message.lastIndexOf(" ", 4090) + 1);
            else newMessage = message.substring(0, 4090);
            VKUser.log.print(newMessage.length() + "");
            message = message.substring(newMessage.length());
            new Message(newMessage).sendMessage(from, to);
            return sendMessage(from, to);
        }


        VKUser.log.print("Send to " + to.getFullName() + ":");
        VKUser.log.print("===============");
        VKUser.log.print(replaceMessageForUser(to));
        VKUser.log.print("===============");
        HashMap<String, String> request = new HashMap<>();
        request.put("user_id", String.valueOf(to.getId()));
        request.put("message", replaceMessageForUser(to));
        if (media.capacity() > 1)
            request.put("attachment", media.toString());
        JSONObject obj = from.getAnswer("messages.send", request);
        if (obj != null && obj.get("error") == null)
            VKUser.log.print("Send successful");
        else {
            new VKException(obj.toJSONString(), from);
            return sendMessage(from, to);
        }
        return true;
    }

    public boolean sendMessage(VKUser from, int userId) {
        return sendMessage(from, new User(userId));
    }

    public String replaceMessageForUser(User user) {
        if (message.contains("%name%") || message.contains("%fullname%") || message.contains("%surname%"))
            return message.replaceAll("%name%", user.getName()).
                    replaceAll("%fullname%", user.getFullName()).replaceAll("%surname%", user.getSurname());
        else return message;
    }

    @Override
    public String toString(){
        return message;
    }
}
