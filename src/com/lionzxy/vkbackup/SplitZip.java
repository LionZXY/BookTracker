package com.lionzxy.vkbackup;

import com.lionzxy.vkapi.VKUser;
import com.lionzxy.vkapi.util.Logger;
import com.lionzxy.vkbackup.io.SplitVKLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * com.lionzxy.vkbackup
 * Created by LionZXY on 31.01.2016.
 * BookTracker
 */
public class SplitZip implements Runnable {
    String name, path;
    public static Logger log = new Logger("[ZIPPING]");
    public List<String> answer;
    int filesSize;
    long finishSize = 0;
    long currSize = 0;
    ZipOutputStream zipOutputStream = null;
    boolean end = false;

    public SplitZip(File file, boolean split, VKUser vkUser) throws IOException {
        this.path = file.getPath();
        this.name = file.getName();
        SplitVKLoader loader = new SplitVKLoader(file, vkUser, split);
        zipOutputStream = new ZipOutputStream(loader);
        List<File> fileList = new ArrayList<>();
        getAllFiles(file, fileList);
        filesSize = fileList.size();
        new Thread(this).start();
        writeAll(fileList);
        end = true;
        loader.close();
        answer = loader.getAnswers();
    }

    public void writeAll(List<File> fileList) throws IOException {
        for (File file : fileList) {
            if (!file.isDirectory())
                addFile(file);
        }
    }

    public void getAllFiles(File dir, List<File> fileList) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                fileList.add(file);
                if (file.isDirectory())
                    getAllFiles(file, fileList);
                else finishSize += file.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        String zipFilePath = file.getCanonicalPath().substring(path.length() + 1,
                file.getCanonicalPath().length());
        ZipEntry zipEntry = new ZipEntry(zipFilePath);
        zipOutputStream.putNextEntry(zipEntry);

        byte[] bytes = new byte[4096];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            currSize += length;
            zipOutputStream.write(bytes, 0, length);
        }
        zipOutputStream.closeEntry();
        fis.close();
    }

    @Override
    public void run() {
        int timeOut = filesSize / 50;
        if (timeOut < 1000) timeOut = 1000;
        try {
            while (!end) {
                Thread.sleep(timeOut);
                float proc = (float) currSize / finishSize;
                StringBuilder sb = new StringBuilder();
                sb.append('[');
                for (float i = 0; i < 1; i += 0.04) {
                    if (proc > i)
                        sb.append('=');
                    else sb.append(' ');
                }
                sb.append(']');
                sb.append(proc * 100).append('%');
                sb.append(" (").append((int) (filesSize * proc)).append('/').append(filesSize).append(')');
                log.print(sb.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.print("Ошибка в потоке отслеживания прогресса архивирования. Ждите или надейтесь на чудо.");
        }
    }

    public static long getSize(File fileCheck) {
        long length = 0;
        if (fileCheck.isDirectory())
            for (File file : fileCheck.listFiles()) {
                if (file.isFile())
                    length += file.length();
                else
                    length += getSize(file);
            }
        else {
            length = fileCheck.length();
        }
        return length;
    }
}
