package com.lionzxy.vkapi.users;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.util.MarketArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LionZXY on 29.12.2015.
 * LeaveBot
 */
public class User {
    int id;
    String name, surname;

    public User(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public User(int id) {
        this.id = id;
    }

    public static List<User> getListUser(List<Integer> users, VKUser vk) {
        if (users.size() < 1000)
            return getListUser(users, vk, 0);
        else {
            List<User> toExit = new ArrayList<>();
            for (int i = 0; i < users.size() / 1000 + 1; i++) {
                toExit.addAll(getListUser(users, vk, i * 1000));
            }
            return toExit;
        }
    }

    public static List<User> getListUser(List<Integer> users, VKUser vk, int from) {
        List<User> usr = new ArrayList<>();
        if (users.size() == 0)
            return usr;
        StringBuilder usersIds = new StringBuilder();
        for (int i = from; i < users.size(); i++) {
            usersIds.append(users.get(i)).append(",");
            from++;
        }
        JSONObject obj2 = vk.getAnswer("users.get?user_ids=" + usersIds.toString());
        JSONArray arr = (JSONArray) obj2.get("response");
        for (Object obj : arr) {
            int id = Integer.parseInt(((JSONObject) obj).get("uid").toString());
            String fN = ((JSONObject) obj).get("first_name").toString();
            String LN = ((JSONObject) obj).get("last_name").toString();
            usr.add(new User(id, fN, LN));
        }
        return usr;
    }

    public User getSurnameAndName() {
        HashMap<String, String> req = new HashMap<>();
        req.put("user_ids", String.valueOf(id));
        JSONObject obj = (JSONObject) ((JSONArray) VKUser.getAnswer("users.get", req, null).get("response")).get(0);
        name = obj.get("first_name").toString();
        surname = obj.get("last_name").toString();
        return this;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        if (name != null || surname != null)
            return name + " " + surname;
        else return String.valueOf(getId());
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public boolean isFriend(VKUser vk) {
        for (Object obj : (JSONArray) vk.getAnswer("friends.get", new HashMap<>()).get("response")) {
            if (Integer.parseInt(obj.toString()) == this.getId())
                return true;
        }
        return false;
    }
}
