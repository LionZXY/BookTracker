package com.lionzxy.vkapi.auth;

/**
 * com.lionzxy.vkapi.auth
 * Created by LionZXY on 02.02.2016.
 * BookTracker
 */
public class TokenAuth implements IAuth {
    String token;
    public TokenAuth(String token){
        this.token = token;
    }

    @Override
    public String getAuthToken() {
        return token;
    }

    @Override
    public void relogging() {
        //LOL
    }
}
