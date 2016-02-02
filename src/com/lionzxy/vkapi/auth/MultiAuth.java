package com.lionzxy.vkapi.auth;

import com.lionzxy.vkapi.util.UsersFile;

import java.io.File;

/**
 * com.lionzxy.vkapi.auth
 * Created by LionZXY on 02.02.2016.
 * BookTracker
 */
public class MultiAuth implements IAuth {
    char splitter;
    String[] logPswd;
    int pos = 0;
    String authToken = null;

    public MultiAuth(File file, char spliter) {
        this.splitter = spliter;
        this.logPswd = UsersFile.getUsers(file.getPath() + "/" + file.getName());
    }

    public MultiAuth(String[] logPswd, char spliter) {
        this.splitter = spliter;
        this.logPswd = logPswd;
    }

    @Override
    public String getAuthToken() {
        return null;
    }

    @Override
    public void relogging() {
        if (logPswd.length == 1)
            return;
        if (logPswd.length - 1 == pos) {
            pos = 0;
            String[] pars = logPswd[pos].split(splitter + "");
            authToken = new LoginPaswordAuth(pars[0],pars[1]).getAuthToken();
        } else {
            pos++;
            String[] pars = logPswd[pos].split(splitter + "");
            authToken = new LoginPaswordAuth(pars[0],pars[1]).getAuthToken();
        }
    }
}
