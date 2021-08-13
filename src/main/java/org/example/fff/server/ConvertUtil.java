package org.example.fff.server;

import java.net.Socket;

public class ConvertUtil {
    public static Request toRequest(Socket income) {
        return new Request();
    }

    public static Response toResponse(Socket income) {
        return new Response();
    }
}
