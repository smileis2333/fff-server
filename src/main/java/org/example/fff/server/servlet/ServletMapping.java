package org.example.fff.server.servlet;

public class ServletMapping {
    private String urlPattern;

    private String servletName;

    public ServletMapping(String urlPattern, String servletName) {
        this.servletName = servletName;
        this.urlPattern = urlPattern;
    }

    public String getServletName() {
        return servletName;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public boolean support(String url) {
        //todo
        return urlPattern.equals(url);
    }
}
