package com.lionzxy.vkapi.event;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.util.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * com.lionzxy.vkapi.event
 * Created by LionZXY on 08.01.2016.
 * BookTracker
 */
public class SubscribeMessageEvent {
    private static List<IMessageListener> list = new ArrayList<>();

    public SubscribeMessageEvent() {

    }

    public void addMessageListener(IMessageListener iMessageListener) {
        if (!list.contains(iMessageListener))
            list.add(iMessageListener);
        else Logger.getLogger().print("TRY ADD DOUBLE LISTENER");
    }

    public void removeMessageListener(IMessageListener iMessageListener) {
        list.remove(iMessageListener);
    }

    public IMessageListener[] getMessageListener() {
        return list.toArray(new IMessageListener[list.size()]);
    }

    public void sendMSG(JSONObject msgJsonObj, VKUser vkUser) {
        if (Integer.parseInt(msgJsonObj.get("read_state").toString()) == 0) {
            boolean inChat = msgJsonObj.get("chat_id") != null;
            if (inChat)
                for (IMessageListener messageListener : list)
                    messageListener.onNewMessageInMultiDialog(new NewMessageEvent(this, new Message(msgJsonObj), vkUser));
            else for (IMessageListener messageListener : list)
                messageListener.onNewMessageInPrivate(new NewMessageEvent(this, new Message(msgJsonObj), vkUser));
        }
    }

    public void checkMessage(VKUser vkUser) {
        VKUser.log.print("Проверка сообщений начата...");
        List<JSONObject> msgObjs = new ArrayList<>();
        while (msgObjs.size() == 0 || Integer.parseInt(msgObjs.get(msgObjs.size() - 1).get("read_state").toString()) == 0) {
            HashMap<String, String> req = new HashMap<>();
            req.put("count", String.valueOf(200));
            req.put("offset", String.valueOf(msgObjs.size()));
            JSONArray array = (JSONArray) vkUser.getAnswer("messages.get", req).get("response");
            for (Object obj : array)
                if (obj instanceof JSONObject)
                    msgObjs.add((JSONObject) obj);
        }

        for (JSONObject jsonObject : msgObjs)
            sendMSG(jsonObject, vkUser);
        VKUser.log.print(msgObjs.size() + " сообщений проверенно");
    }
}
