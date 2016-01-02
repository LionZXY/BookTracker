package com.lionzxy.vkapi.messages;

import com.lionzxy.vkapi.exceptions.VKException;
import com.lionzxy.vkapi.users.User;
import com.lionzxy.vkapi.VKUser;
import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 * Created by LionZXY on 30.12.2015.
 * LeaveBot
 */
public class Message {
    String message;
    StringBuilder media = new StringBuilder();

    public Message(String message) {
        this.message = message;
    }

    public Message addMedia(String media) {
        this.media.append(media).append(',');
        return this;
    }

    public boolean sendMessage(VKUser from, User to) {
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
        VKUser.log.print("Send to " + userId + ":");
        VKUser.log.print("===============");
        VKUser.log.print(message);
        VKUser.log.print("===============");
        HashMap<String, String> request = new HashMap<>();
        request.put("user_id", String.valueOf(userId));
        request.put("message", message);
        if (media.capacity() > 1)
            request.put("attachment", media.toString());
        JSONObject obj = from.getAnswer("messages.send", request);
        if (obj != null && obj.get("error") == null)
            VKUser.log.print("Send successful");
        else {
            new VKException(obj.toJSONString(), from);
            return sendMessage(from, userId);
        }
        return true;
    }

    public String replaceMessageForUser(User user) {
        return message.replaceAll("%name%", user.getName()).
                replaceAll("%fullname%", user.getFullName()).replaceAll("%surname%", user.getSurname());
    }
}
