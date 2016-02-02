package com.lionzxy.vkbackup;


import com.lionzxy.core.string.Split;
import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.documents.VkFile;
import com.lionzxy.vkapi.messages.Message;
import com.lionzxy.vkapi.messages.MessageBuffer;
import com.lionzxy.vkapi.users.User;
import com.lionzxy.vkapi.util.UsersFile;
import com.lionzxy.vkbackup.configs.JsonConfig;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

/**
 * com.lionzxy.vkbackup
 * Created by LionZXY on 27.01.2016.
 * BookTracker
 */
public class Init {
    public static final long maxSize = 199 * 1048576;

    public static void main(String[] args) throws Exception {
        System.out.println(new Date(JsonConfig.getTimeOut("0d 3h 59m 30s")));
        /*VKUser vkUser = new VKUser(UsersFile.getUsers("LeaveBot.usrs")[0]);
        List<String> answer;
        int forID = 76844299;
        File inputFile = new File("C:\\BackUps\\[Backup] vds.lionzxy.ru 20.12.2015\\ZoneAsylum-1.2.21");

        if (SplitZip.getSize(inputFile) > maxSize)
            answer = new SplitZip(inputFile, true, vkUser).answer;
        else answer = new SplitZip(inputFile, false, vkUser).answer;*/
    }
}
