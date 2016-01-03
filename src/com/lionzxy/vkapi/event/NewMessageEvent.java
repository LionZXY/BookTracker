package com.lionzxy.vkapi.event;

import com.lionzxy.vkapi.messages.Message;

import java.util.EventObject;

/**
 * com.lionzxy.vkapi.event
 * Created by LionZXY on 03.01.2016.
 * BookTracker
 */
public class NewMessageEvent extends EventObject {
    Message msg;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public NewMessageEvent(Object source, Message msg) {
        super(source);
        this.msg = msg;

    }
}
