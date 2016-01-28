package com.lionzxy.vkbackup;

import com.lionzxy.vkapi.util.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * com.lionzxy.vkbackup
 * Created by LionZXY on 27.01.2016.
 * BookTracker
 */
public class FileToZip implements Runnable {
    float progress = 0.0F;
    public static Logger log = new Logger("[ZIPPING]");
    int filesSize;
    long maxSize = 1000000;
    long currentSize = 0;
    ZipOutputStream zos;

    private FileToZip(int sizeFile) {
        filesSize = sizeFile;
    }

    public File zipFile(File toZip) throws IOException {
        List<File> fileList = new ArrayList<File>();
        log.print("Getting references to all files in: " + toZip.getCanonicalPath());
        getAllFiles(toZip, fileList);
        new Thread(new FileToZip(fileList.size())).start();
        log.print("Creating zip file");
        File zipFile = writeZipFile(toZip, fileList);
        log.print("Done");
        return zipFile;
    }

    public void getAllFiles(File dir, List<File> fileList) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                fileList.add(file);
                if (file.isDirectory())
                    getAllFiles(file, fileList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File writeZipFile(File directoryToZip, List<File> fileList) {
        try {
            File zipFile = new File(directoryToZip.getName() + ".zip");
            FileOutputStream fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            for (int i = 0; i < fileList.size(); i++) {
                if (!fileList.get(i).isDirectory()) { // we only zip files, not directories
                    progress = (float) (i + 1) / fileList.size();
                    addToZip(directoryToZip, fileList.get(i), zos);
                }
            }

            zos.close();
            fos.close();
            return zipFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,
            IOException {

        FileInputStream fis = new FileInputStream(file);

        // we want the zipEntry's path to be a relative path that is relative
        // to the directory being zipped, so chop off the rest of the path
        String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
                file.getCanonicalPath().length());
        //System.out.println("Writing '" + zipFilePath + "' to zip file");
        ZipEntry zipEntry = new ZipEntry(zipFilePath);
        if (zipEntry.getCompressedSize() + currentSize > maxSize) {
            zos.close();

        }
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

    @Override
    public void run() {
        int timeOut = filesSize / 50;
        try {
            while (progress < 1) {
                Thread.sleep(timeOut);
                StringBuilder sb = new StringBuilder();
                sb.append('[');
                for (float i = 0; i < 1; i += 0.04) {
                    if (progress > i)
                        sb.append('=');
                    else sb.append(' ');
                }
                sb.append(']');
                sb.append(progress * 100).append('%');
                sb.append(" (").append((int) (filesSize * progress)).append('/').append(filesSize).append(')');
                log.print(sb.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.print("Ошибка в потоке отслеживания прогресса архивирования. Ждите или надейтесь на чудо.");
        }
    }
}
