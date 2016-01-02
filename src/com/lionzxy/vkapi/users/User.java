package com.lionzxy.vkapi.users;

import com.lionzxy.vkapi.VKUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
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

    public static List<User> getListUser(List<Integer> users, VKUser vk) {
        if (users.size() > 1000)
            return null;
        List<User> usr = new ArrayList<>();
        if (users.size() == 0)
            return usr;
        StringBuilder usersIds = new StringBuilder();
        for (Integer user : users)
            usersIds.append(user).append(",");
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

    public int getId() {
        return id;
    }

    public String getFullName() {
        return name + " " + surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
