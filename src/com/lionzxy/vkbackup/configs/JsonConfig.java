package com.lionzxy.vkbackup.configs;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.auth.LoginPaswordAuth;
import com.lionzxy.vkapi.auth.TokenAuth;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.core.io.MultiOutput;
import com.lionzxy.vkbackup.io.SplitFileOutput;
import com.lionzxy.vkbackup.io.SplitVKLoader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * com.lionzxy.vkbackup.configs
 * Created by LionZXY on 02.02.2016.
 * BookTracker
 */
public class JsonConfig extends Config {
    Logger log = new Logger("[CONFIG]");
    File jsonFile = new File("config", "VkBackup.json");
    JSONObject jsonObject = new JSONObject();

    public JsonConfig() {
        try {
            if (!jsonFile.exists()) {
                jsonFile.getParentFile().mkdirs();
                jsonFile.createNewFile();
                createNewFile();
            } else {
                jsonObject = (JSONObject) new JSONParser().parse(new FileReader(jsonFile));
            }
        } catch (Exception e) {
            new CrashFileHelper(e);
        }
    }

    void createNewFile() {
        //General
        JSONObject general = new JSONObject();
        general.put("vk", false);
        general.put("file", false);
        general.put("timeout", "0d 2h 59m 59s");
        jsonObject.put("general", general);
        //VK
        JSONObject vk = new JSONObject();
        vk.put("auth-token", "Your can put here authtoken with permission messages and docs. Get auth token : https://oauth.vk.com/token?grant_type=password&client_id=2274003&client_secret=hHbZxrka2uZ6jB1inYsH&username=!!!PASTE_YOUR_LOGIN_HERE!!!&password=!!!PASTE_YOUR_PASSWORD_HERE!!!&scope=docs,messages");
        vk.put("login", "If you want that VKApi can reloging on error, put login here.");
        vk.put("password", "Put password here.");
        vk.put("sendBackupComment", "User vk id. ONLY NUMBER!!!");
        vk.put("sendBackupID", 76844299);
        jsonObject.put("vk", vk);
        //File
        JSONObject file = new JSONObject();
        file.put("splitSizeComment", "Split size in MB");
        file.put("splitSize", -1);
        file.put("backupFolder", "%THIS%/backup/");
        jsonObject.put("file", file);
        log.print("Config file successful!");
        try {
            FileOutputStream os = new FileOutputStream(jsonFile);
            os.write(getFormatedText(jsonObject.toString()).getBytes());
            os.close();
            log.print("Config file successful save!");
        } catch (Exception e) {
            new CrashFileHelper(e);
        }

    }

    public long getTimeOut() {
        long exit = 0;
        String parse = (String) ((JSONObject) jsonObject.get("general")).get("timeout");
        byte zeroB = (byte) '0' - 1, nineB = (byte) '9' + 1;
        StringBuilder sb = new StringBuilder();
        byte symbol;
        for (int i = 0; i < parse.length(); i++) {
            symbol = (byte) parse.charAt(i);
            if (zeroB < symbol && symbol < nineB)
                sb.append(parse.charAt(i));
            else switch (parse.charAt(i)) {
                case 's': {
                    if (sb.length() != 0)
                        exit += Integer.parseInt(sb.toString()) * 1000;
                    sb.delete(0, sb.length());
                    break;
                }
                case 'm': {
                    if (sb.length() != 0)
                        exit += Integer.parseInt(sb.toString()) * 60000;
                    sb.delete(0, sb.length());
                    break;
                }
                case 'h': {
                    if (sb.length() != 0)
                        exit += Integer.parseInt(sb.toString()) * 3600000;
                    sb.delete(0, sb.length());
                    break;
                }
                case 'd': {
                    if (sb.length() != 0)
                        exit += Integer.parseInt(sb.toString()) * 86400000;
                    sb.delete(0, sb.length());
                    break;
                }
            }
        }
        return exit;
    }

    public MultiOutput getMultiOutput() {
        List<OutputStream> streams = new ArrayList<>();
        if ((Boolean) ((JSONObject) jsonObject.get("general")).get("file")) {
            log.print("Detected enable save in file system!");
            try {
                JSONObject file = (JSONObject) jsonObject.get("file");
                String path = (String) file.get("backupFolder");
                if(path.contains("%THIS%"))
                path = System.getProperty("user.dir") + path.substring(path.indexOf("%THIS%") + 6);
                File backupFile = new File(path,"Backup-" + CrashFileHelper.dateToString(new Date()));
                System.out.println(path);
                backupFile.getParentFile().mkdirs();
                if ((Long) file.get("splitSize") > 0) {
                    streams.add(new SplitFileOutput(backupFile, (Long) file.get("splitSize") * 1048576));
                } else streams.add(new FileOutputStream(backupFile));
            } catch (Exception e) {
                new CrashFileHelper(e);
            }
        }
        if ((Boolean) ((JSONObject) jsonObject.get("general")).get("vk")) {
            log.print("Detected enable save in VK server!");
            JSONObject vk = (JSONObject) jsonObject.get("vk");
            int userid = -1;
            VKUser vkUser = null;
            if (!((String) vk.get("auth-token")).contains(" ")) {
                vkUser = new VKUser(new TokenAuth((String) vk.get("auth-token")));
            } else if (!((String) vk.get("login")).contains(" ") && !((String) vk.get("password")).contains(" ")) {
                vkUser = new VKUser(new LoginPaswordAuth((String) vk.get("login"), (String) vk.get("password")));
            } else {
                log.print("!!!YOU MUST ENTER AUTH FOR VKontakte!!!");
            }
            userid = Math.toIntExact((Long) vk.get("sendBackupID"));
            if (vkUser == null || userid == -1) {
                log.print("FAILED CREATE VK OUTPUT STREAM");
            } else {
                try {
                    streams.add(new SplitVKLoader(new File("Backup-" + CrashFileHelper.dateToString(new Date())), vkUser, userid));
                } catch (Exception e) {
                    new CrashFileHelper(e);
                }
            }
        }
        return new MultiOutput(streams);
    }

    public static String getFormatedText(String in) {
        StringBuilder sb = new StringBuilder();
        boolean isIgnore = false;
        int tabCount = 0;
        int b;
        for (int i = 0; i < in.length(); i++) {
            sb.append(in.charAt(i));
            if (in.charAt(i) == '\"')
                isIgnore = !isIgnore;
            if (!isIgnore)
                switch (in.charAt(i)) {
                    case '{':
                    case '[':
                        tabCount++;
                    case ',':
                        sb.append('\n');
                        for (b = 0; b < tabCount; b++)
                            sb.append('\t');
                        break;
                    case '}':
                    case ']':
                        tabCount--;
                        sb.deleteCharAt(sb.length() - 1);
                        sb.append("\n");
                        for (b = 0; b < tabCount; b++)
                            sb.append('\t');
                        sb.append(in.charAt(i));
                }
        }
        return sb.toString();
    }

}
