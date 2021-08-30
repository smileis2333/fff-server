package org.example.fff.server;

import org.example.fff.server.servlet.Request;
import org.example.fff.server.servlet.Response;
import org.example.fff.server.servlet.filter.Chain;
import org.example.fff.server.servlet.filter.FilterMapping;
import org.example.fff.server.util.ResponseHeaderWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public interface Handler {
    void handler(Request request, Response response) throws ServletException, IOException;

    Server getServer();

    void setServer(Server server);

}


class SimpleHandler implements Handler {
    private Server server;

    private Map<String, HttpServlet> servletMap = new HashMap<>();
    private Map<String, String> servletMapping = new HashMap<>();
    private Map<String, Filter> filterMap = new HashMap<>();
    private List<FilterMapping> filterMappings = new ArrayList<>();

    private HttpServlet notFoundServlet = new HttpServlet() {
        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println("找不到哇");
        }
    };

    @Override
    public void handler(Request request, Response response) throws ServletException, IOException {
        HttpServlet servlet = getServletFromPath(request.getServletPath());
        servlet = servlet != null ? servlet : notFoundServlet;

        FilterChain chain = null;
        chain = Chain.newChainEnd(servlet);

        Set<String> wrapCheck = new HashSet<>();

        // wrap url pattern
        // todo support regex
        for (FilterMapping filterMapping : filterMappings) {
            if (filterMapping.supportURLPattern(request.getServletPath())) {
                chain = Chain.newChain(filterMap.get(filterMapping.getFilterName()), chain);
                wrapCheck.add(filterMapping.getFilterName());
            }
        }

        // wrap servlet name
        for (FilterMapping filterMapping : filterMappings) {
            if (!wrapCheck.contains(filterMapping.getFilterName()) && filterMapping.supportURLPattern(request.getServletPath())) {
                chain = Chain.newChain(filterMap.get(filterMapping.getFilterName()), chain);
            }
        }
        chain.doFilter(request,response);
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