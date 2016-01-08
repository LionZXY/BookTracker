package com.litrpg.booktracker.message;

import com.lionzxy.vkapi.event.IMessageListener;
import com.lionzxy.vkapi.event.NewMessageEvent;
import com.lionzxy.vkapi.event.SubscribeMessageEvent;
import com.lionzxy.vkapi.util.Logger;

/**
 * com.litrpg.booktracker.message
 * Created by LionZXY on 08.01.2016.
 * BookTracker
 */
public class MessageListiner implements IMessageListener{
    public static SubscribeMessageEvent sme = new SubscribeMessageEvent();

    public MessageListiner(){
        sme.addMessageListener(this);
    }

    @Override
    public void onNewMessageInPrivate(NewMessageEvent e) {
        Logger.getLogger().print("Recieve Message: " + e.getMsg());
    }

    @Override
    public void onNewMessageInMultiDialog(NewMessageEvent e) {
        Logger.getLogger().print("Recieve Message in multiDialog: " + e.getMsg());
    }
}
