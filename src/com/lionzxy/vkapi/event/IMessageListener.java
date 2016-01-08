package com.lionzxy.vkapi.event;

/**
 * com.lionzxy.vkapi.event
 * Created by LionZXY on 08.01.2016.
 * BookTracker
 */
public interface IMessageListener {

    void onNewMessageInPrivate(NewMessageEvent e);

    void onNewMessageInMultiDialog(NewMessageEvent e);
}
