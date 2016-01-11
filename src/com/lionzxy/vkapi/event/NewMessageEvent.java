package com.lionzxy.vkapi.event;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;

import java.util.EventObject;

/**
 * com.lionzxy.vkapi.event
 * Created by LionZXY on 03.01.2016.
 * BookTracker
 */
public class NewMessageEvent extends EventObject {
    private Message msg = null;
    private VKUser vkUser = null;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public NewMessageEvent(Object source, Message msg, VKUser vkUser) {
        super(source);
        this.msg = msg;
        this.vkUser = vkUser;
    }

    public Message getMsg() {
        return msg;
    }

    public VKUser getVkUser() {
        return vkUser;
    }


}
