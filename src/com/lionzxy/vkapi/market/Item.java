package com.lionzxy.vkapi.market;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.util.UsersFile;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * com.lionzxy.vkapi.market
 * Created by LionZXY on 23.01.2016.
 * BookTracker
 */
public class Item {

    public static int addItem(VKUser vkUser, String name, String descr, int category, int price, VkMarketPhoto... photos) {
        HashMap<String, String> req = new HashMap<>();
        req.put("name", name);
        req.put("description", descr);
        req.put("category_id", String.valueOf(category));
        req.put("price", String.valueOf(price));
        req.put("deleted", "0");
        StringBuilder additionalPhoto = new StringBuilder();
        for (VkMarketPhoto photo : photos)
            if (photo.main) {
                req.put("main_photo_id", photo.photo.get("pid").toString());
                req.put("owner_id", photo.photo.get("owner_id").toString());
            } else {
                if (additionalPhoto.length() != 0) additionalPhoto.append(",");
                additionalPhoto.append(photo.photo.get("pid").toString());
            }
        if (additionalPhoto.length() != 0) req.put("photo_ids", additionalPhoto.toString());
        JSONObject object = vkUser.getAnswer("market.add", req);
        return Math.toIntExact((Long) ((JSONObject) object.get("response")).get("market_item_id"));
    }

    public static int addItem(VKUser vkUser, int groupId, String name, String descr, int category, int price, String... photosLink) {
        List<VkMarketPhoto> photos = new ArrayList<>();
        try {
            photos.add(new VkMarketPhoto(vkUser, groupId, true, UsersFile.getLinkAsFile(photosLink[0])));
            for (int i = 1; i < photosLink.length; i++)
                if (photosLink[i].length() > 1)
                    photos.add(new VkMarketPhoto(vkUser, groupId, false, UsersFile.getLinkAsFile(photosLink[i])));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addItem(vkUser, name, descr, category, price, photos.toArray(new VkMarketPhoto[photos.size()]));
    }
}
