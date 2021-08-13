package org.example.fff.server;

public interface Handler {
    void handler(Request request, Response response);
}


class SimpleHandler implements Handler{

    @Override
    public void handler(Request request, Response response) {
        System.out.println(111);
    }
}