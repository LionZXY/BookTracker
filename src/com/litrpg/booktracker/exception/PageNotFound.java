package com.litrpg.booktracker.exception;

import com.lionzxy.vkapi.messages.Message;

/**
 * com.litrpg.booktracker.exception
 * Created by LionZXY on 09.02.2016.
 * BookTracker
 */
public class PageNotFound extends RuntimeException {
    public static Message msg = new Message("Страничка не найдена.").addMedia("photo286477373_399674456");

    public PageNotFound() {

    }
}
