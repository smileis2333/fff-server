package org.example.fff.server;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        AbstractConnector connector = new ServerConnector(8080);
        connector.setServer(server);
        server.setConnectors(new AbstractConnector[]{connector});
        server.start();
    }
}
