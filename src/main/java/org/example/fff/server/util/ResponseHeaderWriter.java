package org.example.fff.server.util;

import org.example.fff.server.servlet.Response;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class ResponseHeaderWriter extends PrintWriter {
    private boolean isCommit = false;
    private Response response;

    public void setCommit(boolean commit) {
        isCommit = commit;
    }

    public boolean isCommit() {
        return isCommit;
    }

    public ResponseHeaderWriter(OutputStream out, Response response) {
        super(out);
        this.response = response;
    }

    @Override
    public void flush() {
        super.flush();
        isCommit =true;
    }

    public void printHeader(){
        println(response.newMetaInfo());
        flush();

    }
}
