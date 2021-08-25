package org.example.fff.server;

import org.example.fff.server.servlet.Request;
import org.example.fff.server.servlet.Response;
import org.example.fff.server.util.ResponseHeaderWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public interface Handler {
    void handler(Request request, Response response);

    Server getServer();

    void setServer(Server server);

}


class SimpleHandler implements Handler {
    private Server server;

    private Map<String, HttpServlet> servletMap = new HashMap<>();
    private Map<String, String> servletMapping = new HashMap<>();

    private HttpServlet notFoundServlet = new HttpServlet() {
        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println("找不到哇");
        }
    };

    @Override
    public void handler(Request request, Response response) {
        HttpServlet servlet = getServletFromPath(request.getServletPath());
        servlet = servlet != null ? servlet : notFoundServlet;
        try {
            servlet.service(request, response);
            ResponseHeaderWriter headerWriter = response.getHeaderWriter();
            if (!headerWriter.isCommit()) {
                headerWriter.printHeader();
            }
            PrintWriter writer = response.getWriter();
            writer.flush();
            request.getIncome().close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            System.out.println("servlet处理异常");
        }
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }


    public void addServlet(HttpServlet servlet) {
        String servletName = servlet.getServletName();
        servletMap.put(servletName, servlet);
    }

    public void addMapping(String path, String servletName) {
        servletMapping.put(path, servletName);
    }

    public HttpServlet getServletFromName(String servletName) {
        return servletMap.get(servletName);
    }

    public HttpServlet getServletFromPath(String path) {
        return servletMap.get(servletMapping.get(path));
    }
}