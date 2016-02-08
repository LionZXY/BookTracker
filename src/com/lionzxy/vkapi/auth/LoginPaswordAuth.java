package com.lionzxy.vkapi.auth;

import com.lionzxy.vkapi.VKUser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * com.lionzxy.vkapi.auth
 * Created by LionZXY on 02.02.2016.
 * BookTracker
 */
public class LoginPaswordAuth implements IAuth {
    String login,paswd,authtoken = null;


    public LoginPaswordAuth(String loginPswd, char splitter){
        String[] strings = loginPswd.split(splitter + "");
        this.login = strings[0];
        this.paswd = strings[1];
    }

    public LoginPaswordAuth(String login, String paswd){
        this.login = login;
        this.paswd = paswd;
    }
    @Override
    public String getAuthToken() {
        if(authtoken == null){
            relogging();
        }
        return authtoken;
    }

    @Override
    public void relogging() {
        VKUser.log.print("Get authtoken...");
        try {
            String url = "https://oauth.vk.com/token?grant_type=password&client_id=2274003&client_secret=hHbZxrka2uZ6jB1inYsH&" +
                    "username=" + login + "&password=" + paswd+ "&scope=docs,messages,notify,friends,wall,groups,offline";
            URL urlObj;
            InputStream is = null;
            BufferedReader br;
            String line;
            urlObj = new URL(url);
            is = urlObj.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObj = (JSONObject) jsonParser.parse(line);
                authtoken = jsonObj.get("access_token").toString();
            }
            if (is != null) is.close();
            br.close();
        } catch (Exception e) {
            VKUser.sleep(60000);
            relogging();
        }
    }
}
