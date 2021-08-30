package org.example.fff.server;

import org.example.fff.server.demo.DemoServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        AbstractConnector connector = new ServerConnector(8080);
        connector.setServer(server);
        server.setConnectors(new AbstractConnector[]{connector});

        DemoServlet servlet = new DemoServlet();
        server.setScanPackages(new String[]{"org.example.fff.server.demo.DemoServlet"});
        server.start();
    }
}
