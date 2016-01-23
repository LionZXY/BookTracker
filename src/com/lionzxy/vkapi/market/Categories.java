package com.lionzxy.vkapi.market;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.util.ListHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * com.lionzxy.vkapi.market
 * Created by LionZXY on 23.01.2016.
 * BookTracker
 */
public class Categories {
    public static List<Categories> categories = null;
    int id, sectionId;
    String name, sectionName;

    Categories(int id, String name, int sectionId, String sectionName) {
        this.id = id;
        this.sectionId = sectionId;
        this.name = name;
        this.sectionName = sectionName;
    }


    public static void getAllCategories(VKUser vkUser) {
        JSONObject jsonObject = vkUser.getAnswer("market.getCategories", ListHelper.getHashMap("count", String.valueOf(1000)));
        categories = new ArrayList<>();
        ((JSONArray) jsonObject.get("response")).stream().filter(obj -> obj instanceof JSONObject).forEach(obj -> {
            JSONObject cat = (JSONObject) obj;
            JSONObject secCat = (JSONObject) cat.get("section");
            categories.add(new Categories(Math.toIntExact((Long) cat.get("id")),
                    cat.get("name").toString(),
                    Math.toIntExact((Long) secCat.get("id")),
                    secCat.get("name").toString()));
        });
    }

    public static int getIdOnName(VKUser vkUser, String catName) {
        if(categories == null)
            getAllCategories(vkUser);
        for(Categories cat : categories){
            if(cat.name.equalsIgnoreCase(catName))
                return cat.id;
        }
        return -1;
    }

    public static int getIdOnName(VKUser vkUser, String catName, int defaultID) {
        int resp = getIdOnName(vkUser, catName);
        return resp == -1 ? defaultID : resp;
    }
}
