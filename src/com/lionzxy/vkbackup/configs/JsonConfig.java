package com.lionzxy.vkbackup.configs;

import com.lionzxy.core.crash.CrashFileHelper;
import org.json.simple.JSONObject;

import java.io.File;

/**
 * com.lionzxy.vkbackup.configs
 * Created by LionZXY on 02.02.2016.
 * BookTracker
 */
public class JsonConfig extends Config {
    File jsonFile = new File("config", "VkBackup.json");
    JSONObject obj;
    public JsonConfig() {
        try {
            if (!jsonFile.exists()) {
                jsonFile.getParentFile().mkdirs();
                jsonFile.createNewFile();
            }
        } catch (Exception e) {
            new CrashFileHelper(e);
        }
    }

    void createNewFile() {
        JSONObject jsonObject = new JSONObject();
        //General
        JSONObject general = new JSONObject();
        general.put("vk", false);
        general.put("file", false);
        general.put("timeout","0d 2h 59m 59s");
        jsonObject.put("general", general);
        //VK
        JSONObject vk = new JSONObject();
        vk.put("auth-token", "Your can put here authtoken with permission messages and docs. Get auth token : \"https://oauth.vk.com/token?grant_type=password&client_id=2274003&client_secret=hHbZxrka2uZ6jB1inYsH&username=!!!PASTE_YOUR_LOGIN_HERE!!!&password=!!!PASTE_YOUR_PASSWORD_HERE!!!&scope=docs,messages\"");
        vk.put("login", "If you want that VKApi can reloging on error, put login here.");
        vk.put("password", "Put password here.");
        vk.put("sendBackupComment","User vk id. ONLY NUMBER!!!");
        vk.put("sendBackupID",76844299);
        jsonObject.put("vk", vk);
        //File
        JSONObject file = new JSONObject();
        file.put("splitSize", -1);
        file.put("backupFolder", "%THIS%/backup/");
        jsonObject.put("file", file);
    }

    public static long getTimeOut(String parse){
        long exit = 0;
        //String parse = (String) ((JSONObject)obj.get("general")).get("timeout");
        byte zeroB = (byte) '0' - 1,nineB = (byte) '9' + 1;
        StringBuilder sb = new StringBuilder();
        byte symbol;
        for(int i = 0; i < parse.length(); i++){
            symbol = (byte) parse.charAt(i);
            if(zeroB < symbol && symbol < nineB)
                sb.append(parse.charAt(i));
            else switch (parse.charAt(i)){
                case 's': {
                    if(sb.length() != 0)
                    exit += Integer.parseInt(sb.toString()) * 1000;
                    sb.delete(0,sb.length());
                    break;
                }case 'm': {
                    if(sb.length() != 0)
                    exit += Integer.parseInt(sb.toString()) * 60000;
                    sb.delete(0,sb.length());
                    break;
                }case 'h': {
                    if(sb.length() != 0)
                    exit += Integer.parseInt(sb.toString()) * 3600000;
                    sb.delete(0,sb.length());
                    break;
                }case 'd': {
                    if(sb.length() != 0)
                    exit += Integer.parseInt(sb.toString()) * 86400000;
                    sb.delete(0,sb.length());
                    break;
                }
            }
        }
        return exit;
    }
}
