package com.litrpg.booktracker.message.botanswer;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.messages.Message;

/**
 * com.litrpg.booktracker.message.botanswer
 * Created by LionZXY on 11.01.2016.
 * BookTracker
 */
public interface IAnswer {

    void onMessage(Message msg, VKUser vkUser);
}
