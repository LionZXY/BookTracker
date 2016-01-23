package com.lionzxy.vkapi.documents;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.util.MultipartUtility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;

/**
 * com.lionzxy.vkapi.documents
 * Created by LionZXY on 17.01.2016.
 * BookTracker
 */
public class VkFile {
    int bookId, ownerId, size;
    String url;

    public VkFile(File file, VKUser vkUser) {
        this(vkUser, getFile(vkUser, file), file.getName());
    }

    public VkFile(VKUser vkUser, String file, String fileName) {
        HashMap<String, String> req = new HashMap<>();
        req.put("file", file);
        req.put("title", fileName);
        req.put("tags", "book");
        JSONObject answer = (JSONObject) ((JSONArray) vkUser.getAnswer("docs.save", req).get("response")).get(0);
        bookId = Math.toIntExact((Long) answer.get("did"));
        ownerId = Math.toIntExact((Long) answer.get("owner_id"));
        url = (String) answer.get("url");
        size = Math.toIntExact((Long) answer.get("size"));
    }

    public static String getFile(VKUser vkUser, File file) {
        try {
            return (String) ((JSONObject) new JSONParser().parse(
                    new StringReader(new MultipartUtility(((JSONObject) vkUser.getAnswer("docs.getUploadServer", null).get("response")).get("upload_url").toString(), "UTF-8").addFilePart("file", file).finish().get(0)))).get("file");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getAsVkMedia() {
        return "doc" + ownerId + "_" + bookId;
    }
}
