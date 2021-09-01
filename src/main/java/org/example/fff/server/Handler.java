package org.example.fff.server;

import org.example.fff.server.servlet.Request;
import org.example.fff.server.servlet.Response;
import org.example.fff.server.servlet.ServletMapping;
import org.example.fff.server.servlet.filter.Chain;
import org.example.fff.server.servlet.filter.FilterMapping;
import org.example.fff.server.util.FilterMappingType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public interface Handler {
    void handler(Request request, Response response) throws ServletException, IOException;

    Server getServer();

    void setServer(Server server);

}


class SimpleHandler implements Handler {
    private Server server;

    private Map<String, HttpServlet> servletMap = new HashMap<>();
    private Map<String, HttpFilter> filterMap = new HashMap<>();

    private List<ServletMapping> servletMappings = new ArrayList<>();
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
        chain.doFilter(request, response);
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }


    public void addServlet(Class<? extends HttpServlet> clazz) {
        for (Annotation annotation : clazz.getClass().getAnnotations()) {
            if (annotation.annotationType() == WebServlet.class) {
                try {
                    HttpServlet servlet = clazz.getConstructor().newInstance();
                    String servletName = (String) WebServlet.class.getMethod("name").invoke(annotation);
                    String[] values = (String[]) WebServlet.class.getMethod("value").invoke(annotation);
                    addServlet(servletName, servlet);
                    for (String value : values) {
                        addServletMapping(value, servletName);
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addServlet(String servletName, HttpServlet servlet) {
        servletMap.put(servletName, servlet);
    }

    private void addServletMapping(String urlPattern, String servletName) {
        servletMappings.add(new ServletMapping(urlPattern, servletName));
    }

    public void addFilter(Class<HttpFilter> clazz) {
        for (Annotation annotation : clazz.getClass().getAnnotations()) {
            if (annotation.annotationType() == WebFilter.class) {
                try {
                    HttpFilter servlet = clazz.getConstructor().newInstance();
                    String filterName = (String) WebFilter.class.getMethod("filterName").invoke(annotation);
                    String[] values = (String[]) WebFilter.class.getMethod("urlPatterns").invoke(annotation);
                    String[] servletNames = (String[]) WebFilter.class.getMethod("servletNames").invoke(annotation);
                    addFilter(filterName, servlet);

                    for (String value : values) {
                        addFilterMapping(FilterMappingType.URL_PATTERN, filterName, value);
                    }


                    for (String value : servletNames) {
                        addFilterMapping(FilterMappingType.SERVLET_NAME, filterName, value);
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addFilter(String filterName, HttpFilter filter) {
        filterMap.put(filterName, filter);
    }

    private void addFilterMapping(FilterMappingType filterMappingType, String filterName, String target) {
        if (filterMappingType == FilterMappingType.URL_PATTERN) {
            filterMappings.add(FilterMapping.newUrlPatternMapping(filterMappingType, filterName, target));
        } else if (filterMappingType == FilterMappingType.SERVLET_NAME) {
            filterMappings.add(FilterMapping.newServletMapping(filterMappingType, filterName, target));
        }
    }

    public HttpServlet getServletFromName(String servletName) {
        return servletMap.get(servletName);
    }

    public HttpServlet getServletFromPath(String path) {
        for (ServletMapping servletMapping : servletMappings) {
            if (servletMapping.support(path)) {
                return servletMap.get(servletMapping.getServletName());
            }
        }
        return notFoundServlet;
    }
}