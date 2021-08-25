package org.example.fff.server.util;

import org.example.fff.server.servlet.Response;

import java.io.*;

public class ResponseWriter extends PrintWriter {
    private Response response;

    public ResponseWriter(OutputStream out,Response response) {
        super(out);
        this.response = response;
    }

    @Override
    public void flush() {
        super.flush();
    }
}
