package com.lionzxy.vkapi.market;


import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.util.MultipartUtility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.util.HashMap;

/**
 * com.lionzxy.vkapi.documents
 * Created by LionZXY on 23.01.2016.
 * BookTracker
 */
public class VkMarketPhoto {
    public JSONObject photo;
    public boolean main;

    public VkMarketPhoto(VKUser vkUser, int groupId, boolean main, File file) throws Exception {
        HashMap<String, String> req = new HashMap<>();
        req.put("group_id", String.valueOf(groupId));
        req.put("main_photo", String.valueOf(main ? 1 : 0));
        String uploadUrl = ((JSONObject) vkUser.getAnswer("photos.getMarketUploadServer", req).get("response")).get("upload_url").toString();
        MultipartUtility fileMultiPart = new MultipartUtility(uploadUrl, "UTF-8");
        fileMultiPart.addFilePart("file", file);
        JSONObject photo = (JSONObject) new JSONParser().parse(fileMultiPart.finish().get(0));
        req = new HashMap<>();
        req.put("group_id", String.valueOf(groupId));
        req.put("photo", photo.get("photo").toString());
        req.put("server", photo.get("server").toString());
        req.put("hash", photo.get("hash").toString());
        req.put("crop_data", photo.get("crop_data").toString());
        req.put("crop_hash", photo.get("crop_hash").toString());
        JSONObject answer = vkUser.getAnswer("photos.saveMarketPhoto", req);
        this.photo = (JSONObject) ((JSONArray) answer.get("response")).get(0);
        this.main = main;
    }
}
