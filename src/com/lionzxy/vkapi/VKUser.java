package com.lionzxy.vkapi;

import com.lionzxy.vkapi.exceptions.VKException;
import com.lionzxy.vkapi.util.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * com.lionzxy.leavebot.vk
 * Created by LionZXY on 29.12.2015.
 * LeaveBot
 */
public class VKUser {

    String accesToken, login, password;
    public static Logger log;

    public VKUser(String login, String password, Logger logger) {
        this.login = login;
        this.password = password;
        log = logger;
        getAccesToken();
    }

    @Deprecated
    public JSONObject getAnswer(String request) {
        String method = request.substring(0, request.indexOf('?'));
        String[] params = request.substring(request.indexOf('?') + 1).split("&");
        HashMap<String, String> paramsHash = new HashMap<>();
        for (String param : params)
            paramsHash.put(param.substring(0, param.indexOf('=')), param.substring(param.indexOf('=') + 1));
        return getAnswer(method, paramsHash);
    }

    public JSONObject getAnswer(String method, HashMap<String, String> params) {
        try {
            try {
                try {
                    URL url = new URL("https://api.vk.com/method/" + method + "?access_token=" + accesToken);

                    StringBuilder postData = new StringBuilder();
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
                    conn.setRequestProperty("charset", "utf-8");
                    conn.setRequestProperty("Content-Length", Integer.toString(postDataBytes.length));

                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    wr.write(postDataBytes);
                    wr.flush();
                    wr.close();


                    DataInputStream is = new DataInputStream(conn.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String answer = br.readLine();
                    JSONObject exit = (JSONObject) new JSONParser().parse(new StringReader(answer));
                    if (VKException.isException(answer))
                        throw new VKException(answer, this);
                    br.close();
                    sleep(1000);
                    return exit;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (IOException e) {
                sleep(60000);
                e.printStackTrace();
                getAnswer(method, params);
            }
        } catch (VKException e) {
            e.printStackTrace();
            if (e.isFix)
                getAnswer(method, params);
        }
        return null;
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

    public void getAccesToken() {
        try {
            String url = "https://oauth.vk.com/token?grant_type=password&client_id=2274003&client_secret=hHbZxrka2uZ6jB1inYsH&" +
                    "username=" + login + "&password=" + password + "&scope=notify,friends,wall,groups,offline";
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
                accesToken = jsonObj.get("access_token").toString();
                log.print(accesToken);
            }
            if (is != null) is.close();
        } catch (Exception e) {
            getAccesToken();
        }

    }
}
