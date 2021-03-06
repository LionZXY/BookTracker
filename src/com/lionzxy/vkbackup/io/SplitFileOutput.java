package com.lionzxy.vkbackup.io;

import com.lionzxy.vkapi.util.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * com.lionzxy.vkbackup
 * Created by LionZXY on 31.01.2016.
 * BookTracker
 */
public class SplitFileOutput extends OutputStream {
    static Logger log = new Logger("[SPLITTER]");
    String part = ".";
    String name, path;
    long maxSize;
    long curSize = 0;
    int currentPart = 1;
    OutputStream fileOutputStream = null;

    protected SplitFileOutput(String name, String path, long split) throws IOException {
        this.name = name;
        this.path = path;
        this.maxSize = split;
        createNewOutputStream();
    }

    public SplitFileOutput(File file, long split) throws IOException {
        this.name = file.getName();
        this.path = file.getPath();
        this.maxSize = split;
        createNewOutputStream();
    }

    public SplitFileOutput() {
    }

    @Override
    public void write(byte b[]) throws IOException {
        curSize += b.length;
        if (curSize >= maxSize)
            createNewOutputStream();
        fileOutputStream.write(b, 0, b.length);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        curSize += len;
        if (curSize >= maxSize)
            createNewOutputStream();
        fileOutputStream.write(b, off, len);
    }

    @Override
    public void write(int b) throws IOException {
        curSize++;
        if (curSize >= maxSize)
            createNewOutputStream();
        fileOutputStream.write(b);
    }

    public void createNewOutputStream() throws IOException {
        if (fileOutputStream != null)
            fileOutputStream.close();
        log.print("Create new file: " + name + part + currentPart);
        File file = new File(path,name + part + currentPart);
        file.getParentFile().mkdirs();
        file.createNewFile();
        fileOutputStream = new FileOutputStream(file);
        currentPart++;
        curSize = 0;
    }

    @Override
    public void flush() throws IOException {
        fileOutputStream.flush();
    }

    @Override
    public void close() throws IOException {
        fileOutputStream.close();
    }
}
