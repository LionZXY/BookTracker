package com.lionzxy.vkbackup;

import com.lionzxy.vkbackup.configs.JsonConfig;

import java.io.File;
import java.util.Scanner;

/**
 * com.lionzxy.vkbackup
 * Created by LionZXY on 27.01.2016.
 * BookTracker
 */
public class Init {

    public static void main(String[] args) throws Exception {
        JsonConfig config = new JsonConfig();
        File file;
        if (args.length == 0) {
            System.out.println("Enter filepath/folderpath:");
            file = new File(new Scanner(System.in).nextLine());
        } else file = new File(args[0]);
        new SplitZip(file, config.getMultiOutput());
    }
}
