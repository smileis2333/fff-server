package org.example.fff.server;
import java.io.IOException;
import java.net.Socket;

public class ConvertUtil {
    public static Request toRequest(Socket income) {
        try {
            byte[] bytes = income.getInputStream().readAllBytes();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Request();
    }

    public static Response toResponse(Socket income) {
        return new Response();
    }
}
