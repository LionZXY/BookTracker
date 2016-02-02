package com.lionzxy.vkbackup.io;

import com.lionzxy.core.crash.CrashFileHelper;
import com.lionzxy.core.string.Split;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.documents.VkFile;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.lionzxy.vkapi.users.User;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.vkapi.util.MultipartUtility;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * com.lionzxy.vkbackup.io
 * Created by LionZXY on 31.01.2016.
 * BookTracker
 */
public class SplitVKLoader extends SplitFileOutput {
    List<String> answers = new ArrayList<>();
    MultipartUtility multipartUtility = null;
    VKUser vkUser = null;
    int userid;

    public SplitVKLoader(File file, VKUser vkUser, int userid) throws IOException {
        this.vkUser = vkUser;
        this.name = file.getName();
        this.path = "";
        this.userid = userid;
        this.maxSize = 199 * 1048576;
        log = new Logger("[VKUPLOADER]");
        createNewOutputStream();
    }

    public void createNewOutputStream() throws IOException {
        if (fileOutputStream != null && multipartUtility != null) {
            multipartUtility.finishFilePart();
            log.print("Upload file...");
            for (String tmp : multipartUtility.finish()) {
                answers.add(tmp);
            }
            log.print("File uploading!");
        }
        String filename = name + part + currentPart;
        log.print("Upload new file: " + filename);
        JSONObject obj1 = vkUser.getAnswer("docs.getUploadServer", null);
        JSONObject response = (JSONObject) obj1.get("response");
        Object object = response.get("upload_url");
        String str = object.toString();
        multipartUtility = new MultipartUtility(str, "UTF-8");
        multipartUtility.generateFilePart("file", filename);
        fileOutputStream = multipartUtility.outputStream;
        currentPart++;
        curSize = 0;
    }

    @Override
    public void close() throws IOException {
        multipartUtility.finishFilePart();
        log.print("Upload file...");
        for (String tmp : multipartUtility.finish()) {
            answers.add(tmp);
        }
        try {
            JSONParser parser = new JSONParser();
            for (String file : answers) {
                String fileS = (String) ((JSONObject) parser.parse(new StringReader(file))).get("file");
                MessageBuffer.addMessage(new Message("Файл от " + new Date()).addMedia(new VkFile(vkUser, fileS, Split.spilitToByte(fileS, (byte) 124)[7]).getAsVkMedia()), new User(userid));
            }
            MessageBuffer.flush(vkUser);
        } catch (Exception e) {
            new CrashFileHelper(e);
        }
    }

}
