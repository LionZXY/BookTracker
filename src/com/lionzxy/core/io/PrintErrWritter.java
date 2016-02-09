package com.lionzxy.core.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * com.lionzxy.core.io
 * Created by LionZXY on 09.02.2016.
 * BookTracker
 */
public class PrintErrWritter extends PrintWriter {
    public PrintErrWritter() {
        super(System.err);
    }

    public void close() {
    }
}
