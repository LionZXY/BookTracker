package com.lionzxy.vkapi.documents;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.util.ListHelper;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.vkapi.util.MultipartUtility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * com.lionzxy.vkapi.documents
 * Created by LionZXY on 06.02.2016.
 * BookTracker
 */
public class VkPhotoMessage {
    String id;
    public VkPhotoMessage(File file, VKUser vkUser){
        try {
            List<String> str = MultipartUtility.getURLAndUpload("photos.getMessagesUploadServer",vkUser).addFilePart("photo",file).finish();
            JSONObject obj = (JSONObject) new JSONParser().parse(str.get(0));
            HashMap<String,String> req = new HashMap<>();
            req.put("photo",obj.get("photo").toString());
            req.put("server",obj.get("server").toString());
            req.put("hash",obj.get("hash").toString());
            JSONObject answ =(JSONObject) ((JSONArray) vkUser.getAnswer("photos.saveMessagesPhoto", req).get("response")).get(0);
            id = answ.get("id").toString();
            Logger.getLogger().print("File " + file.getName() + " upload successful");
        } catch (Exception e) {
            new CrashFileHelper(e);
        }
    }

    public String getId(){
        return id;
    }
}
