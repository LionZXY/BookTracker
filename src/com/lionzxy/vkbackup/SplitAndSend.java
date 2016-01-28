package com.lionzxy.vkbackup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * com.lionzxy.vkbackup
 * Created by LionZXY on 27.01.2016.
 * BookTracker
 */
public class SplitAndSend {


    private ZipOutputStream zipOutputStream;

    private String path;
    private String name;

    private long currentSize;
    private int currentChunkIndex;
    private final long MAX_FILE_SIZE = 16000000; // Whatever size you want
    private final String PART_POSTFIX = ".part.";
    private final String FILE_EXTENSION = ".zip";

    public SplitAndSend(String path, String name) throws FileNotFoundException {
        this.path = path;
        this.name = name;
        constructNewStream();
    }

    public void addEntry(ZipEntry entry) throws IOException {
        long entrySize = entry.getCompressedSize();
        if((currentSize + entrySize) > MAX_FILE_SIZE) {
            closeStream();
            constructNewStream();
        } else {
            currentSize += entrySize;
            zipOutputStream.putNextEntry(entry);
        }
    }

    private void closeStream() throws IOException {
        zipOutputStream.close();
    }

    private void constructNewStream() throws FileNotFoundException {
        zipOutputStream = new ZipOutputStream(new FileOutputStream(new File(path, constructCurrentPartName())));
        currentChunkIndex++;
        currentSize = 0;
    }

    private String constructCurrentPartName() {
        // This will give names is the form of <file_name>.part.0.zip, <file_name>.part.1.zip, etc.
        StringBuilder partNameBuilder = new StringBuilder(name);
        partNameBuilder.append(PART_POSTFIX);
        partNameBuilder.append(currentChunkIndex);
        partNameBuilder.append(FILE_EXTENSION);
        return partNameBuilder.toString();
    }
}
