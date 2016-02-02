package com.lionzxy.vkbackup.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * com.lionzxy.vkbackup.io
 * Created by LionZXY on 02.02.2016.
 * BookTracker
 */
public class MultiOutput extends OutputStream {

    List<OutputStream> streams;

    public MultiOutput(List<OutputStream> streams){
        this.streams = streams;
    }

    @Override
    public void write(int b) throws IOException {
        for(OutputStream stream : streams)
            stream.write(b);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        for(OutputStream stream : streams)
            stream.write(b,off,len);
    }

    @Override
    public void flush() throws IOException {
        for(OutputStream stream : streams)
            stream.flush();
    }

    @Override
    public void close() throws IOException {
        for(OutputStream stream : streams)
            stream.close();
    }
}
