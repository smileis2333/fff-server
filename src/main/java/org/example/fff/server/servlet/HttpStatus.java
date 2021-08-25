package org.example.fff.server.servlet;

public class HttpStatus {
    public final static int NOT_FOUND = 404;
    public final static int OK = 200;

    public static String getMessage(int code){
        if (NOT_FOUND == code) {
            return "not found";
        }
        return "";
    }
}
