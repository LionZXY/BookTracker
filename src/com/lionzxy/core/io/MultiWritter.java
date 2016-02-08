package com.lionzxy.core.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * com.lionzxy.core.io
 * Created by LionZXY on 08.02.2016.
 * BookTracker
 */
public class MultiWritter extends Writer {
    List<Writer> wr = new ArrayList<>();

    public MultiWritter(List<Writer> wr) {
        this.wr = wr;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        for(Writer w : wr)
            w.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        for (Writer w : wr)
            w.flush();
    }

    @Override
    public void close() throws IOException {
        for (Writer w : wr)
            w.close();
    }
}
