package com.litrpg.booktracker.message.botanswer;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;

/**
 * com.litrpg.booktracker.message.botanswer
 * Created by LionZXY on 11.01.2016.
 * BookTracker
 */
public class SimplyAnswer implements IAnswer {
    String quest, answr;

    public SimplyAnswer(String quest, String answr) {
        this.quest = quest;
        this.answr = answr;
    }

    @Override
    public void onMessage(Message msg, VKUser vkUser) {
        if (msg.toString().length() > quest.length() &&
                msg.toString().substring(0, quest.length()).equalsIgnoreCase(quest))
            MessageBuffer.addMessage(new Message(answr), msg.getUser());
    }
}
