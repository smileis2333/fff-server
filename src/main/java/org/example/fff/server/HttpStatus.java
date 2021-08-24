package org.example.fff.server;

public class HttpStatus {
    public final static int NOT_FOUND = 404;

    public static String getMessage(int code){
        if (NOT_FOUND == code) {
            return "not found";
        }
        return "";
    }
}
