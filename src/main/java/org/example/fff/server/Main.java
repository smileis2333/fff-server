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
        servlet.init(new ServletConfig() {
            @Override
            public String getServletName() {
                return "test";
            }

            @Override
            public ServletContext getServletContext() {
                return null;
            }

            @Override
            public String getInitParameter(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return null;
            }
        });


        server.addServlet("/test", servlet);
        server.start();
    }
}
