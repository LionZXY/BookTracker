package com.lionzxy.vkapi.auth;

/**
 * com.lionzxy.vkapi.auth
 * Created by LionZXY on 02.02.2016.
 * BookTracker
 */
public interface IAuth {
    String getAuthToken();
    void relogging();
}
