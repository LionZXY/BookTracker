package com.lionzxy.vkapi;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.vkapi.auth.IAuth;
import com.lionzxy.vkapi.exceptions.VKException;
import com.lionzxy.vkapi.util.ListHelper;
import com.lionzxy.vkapi.util.Logger;
import com.sun.istack.internal.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static java.lang.Math.toIntExact;

/**
 * com.lionzxy.leavebot.vk
 * Created by LionZXY on 29.12.2015.
 * LeaveBot
 */
public class VKUser {

    IAuth auth;
    public static Logger log = new Logger("[VKAPI]");
    public int nowOnAcc = 0, messageSendNotFriend = 0;
    public Stack<String> accounts = new Stack<>();

    public VKUser(IAuth auth) {
        this.auth = auth;
    }

    public IAuth getAuth() {
        return auth;
    }

    @Deprecated
    public JSONObject getAnswer(String request) {
        String method = request.substring(0, request.indexOf('?'));
        String[] params = request.substring(request.indexOf('?') + 1).split("&");
        HashMap<String, String> paramsHash = new HashMap<>();
        for (String param : params)
            paramsHash.put(param.substring(0, param.indexOf('=')), param.substring(param.indexOf('=') + 1));
        return getAnswer(method, paramsHash, this);
    }

    public JSONObject getAnswer(String method, HashMap<String, String> params) {
        return getAnswer(method, params, this);
    }

    public static JSONObject getAnswer(String method, HashMap<String, String> params, @Nullable VKUser vkUser) {
        try {
            try {
                try {
                    try {
                        return getAnswerWithThrow(method, params, vkUser);
                    } catch (ParseException e) {
                        new CrashFileHelper(e);
                        return null;
                    }
                } catch (IOException e) {
                    sleep(60000);
                    new CrashFileHelper(e);
                    return getAnswer(method, params, vkUser);
                }
            } catch (VKException e) {
                new CrashFileHelper(e);
                if (e.isFix)
                    return getAnswer(method, params, vkUser);
            }
        } catch (Exception e) {
            new CrashFileHelper(e);
            log.print("Не удалось получить ответ на запрос");
        }
        return null;
    }

    public static JSONObject getAnswerWithThrow(String method, HashMap<String, String> params, @Nullable VKUser vkUser) throws Exception {
        URL url;
        if (vkUser != null)
            url = new URL("https://api.vk.com/method/" + method + "?access_token=" + vkUser.auth.getAuthToken());
        else {
            url = new URL("https://api.vk.com/method/" + method + '?');
        }
        StringBuilder postData = new StringBuilder();
        if (params != null)
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "UTF-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataBytes.length));
        DataOutputStream wr;
        wr = openConn(conn);
        wr.write(postDataBytes);
        wr.flush();
        wr.close();


        DataInputStream is = new DataInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String answer = br.readLine();
        JSONObject exit = (JSONObject) new JSONParser().parse(new StringReader(answer));
        if (VKException.isException(answer))
            throw new VKException(answer, vkUser);
        br.close();
        sleep(1000);
        return exit;
    }

    public List<Integer> getUserList(int groupId) {
        String answer;
        List<Integer> ids = new ArrayList<>();
        JSONObject obj2 = getAnswer("groups.getMembers?group_id=" + groupId + "&offset=0");
        JSONObject obj = (JSONObject) obj2.get("response");
        int count = Integer.parseInt(obj.get("count").toString());
        addToList((JSONArray) obj.get("users"), ids);
        for (int i = 1; i < (count / 1000 + 1); i++) {
            JSONObject obj1 = (JSONObject) getAnswer("groups.getMembers?group_id=" + groupId + "&offset=" + 1000 * i).get("response");
            addToList((JSONArray) obj1.get("users"), ids);
        }
        return ids;
    }

    public static void addToList(JSONArray array, List<Integer> list) {
        for (Object obj : array) {
            if (obj != null)
                list.add(Integer.parseInt(obj.toString()));
        }
    }

    public static void sleep(int sleepInMs) {
        try {
            Thread.sleep(sleepInMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getVkID() {
        return toIntExact((Long) ((JSONObject) ((JSONArray) this.getAnswer("users.get", new HashMap<>()).get("response")).get(0)).get("uid"));
    }

    List<Integer> leaveFromChat = new ArrayList<>();

    public boolean inChat(int chatId) {
        int id = this.getVkID();
        if (leaveFromChat.contains(chatId))
            return false;
        else
            for (Object obj : (JSONArray) ((JSONObject) getAnswer("messages.getChat", ListHelper.getHashMap("chat_id", String.valueOf(chatId))).get("response")).get("users")) {
                if (toIntExact((Long) obj) == id)
                    return true;
            }
        leaveFromChat.add(chatId);
        return false;
    }

    public static DataOutputStream openConn(HttpURLConnection conn) {
        DataOutputStream os = null;
        int timeout = 1000;
        while (os == null) {
            try {
                conn.setConnectTimeout(timeout);
                os = new DataOutputStream(conn.getOutputStream());
            } catch (Exception e) {
                log.print("Ошибка соединения.");
            }
            timeout = timeout * 2;
        }
        return os;
    }

    public void applyFriendRequest() {
        JSONArray arr = (JSONArray) getAnswer("friends.getRequests", ListHelper.getHashMap("need_viewed", "1")).get("response");
        for (Object obj : arr) {
            getAnswer("friends.add", ListHelper.getHashMap("user_id", obj.toString()));
            log.print("Пользователь " + obj + " добавлен в друзья");
        }
    }
}
