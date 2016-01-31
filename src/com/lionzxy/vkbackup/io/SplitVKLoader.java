package com.lionzxy.vkbackup.io;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.util.MultipartUtility;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    boolean split = true;

    public SplitVKLoader(File file, VKUser vkUser, boolean split) throws IOException {
        this.vkUser = vkUser;
        this.name = file.getName();
        this.path = "";
        this.split = split;
        createNewOutputStream();
    }

    public void createNewOutputStream() throws IOException {
        if (fileOutputStream != null && multipartUtility != null) {
            multipartUtility.finishFilePart();
            log.print("Upload file...");
            for (String tmp : multipartUtility.finish()) {
                answers.add(tmp);
            }
        }
        String filename = name + format;
        if (split)
            filename += part + currentPart;
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
    }

    public List<String> getAnswers() {
        return answers;
    }

}
