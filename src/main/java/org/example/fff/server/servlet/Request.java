package org.example.fff.server.servlet;

import org.example.fff.server.util.SessionRegistry;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Request implements HttpServletRequest {
    private Socket income;
    private String servletPath;
    private String method;
    private String protocol;
    private Map<String, List<String>> headerFields = new HashMap<>();
    private Session session;
    private SessionRegistry sessionRegistry;
    private String sessionId;
    private Response response;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public Request() {
    }

    public Socket getIncome() {
        return income;
    }

    public void setIncome(Socket income) {
        this.income = income;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        List<String> cstr = headerFields.get("Cookie");
        if (cstr != null && !cstr.isEmpty()) {
            return Arrays.stream(cstr.get(0).split("; ")).map(e -> {
                String[] split = e.split("=");
                return new Cookie(split[0], split[1]);
            }).collect(Collectors.toList()).toArray(new Cookie[]{});
        }
        return new Cookie[]{};
    }

    @Override
    public long getDateHeader(String name) {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        return getHeaders(name).nextElement();
    }

    private Cookie[] cookies;

    public void setHeader(String name, String value) {
        setHeader(name, List.of(value));
        if ("Cookie".equals(name)) {
            String[] cstr = value.split("; ");
            cookies = new Cookie[cstr.length];
            for (int i = 0; i < cstr.length; i++) {
                String[] nv = cstr[i].split("=");
                cookies[i] = new Cookie(nv[0], nv[1]);
            }
        }
    }

    public void setHeader(String name, List<String> value) {
        headerFields.put(name, value);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return Collections.enumeration(headerFields.getOrDefault(name, Collections.emptyList()));
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(headerFields.keySet());
    }

    @Override
    public int getIntHeader(String name) {
        return 0;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    @Override
    public HttpSession getSession(boolean create) {
        Session session = null;
        if (sessionId != null) {
            session = sessionRegistry.getSession(sessionId);
        }

        if (session != null) {
            return session;
        }

        if (create) {
            session = Session.newSession(sessionRegistry);
            this.sessionId = session.getId();
            response.addCookie(new Cookie("sessionId", session.getId()));
        }

        this.session = session;
        return session;
    }

    @Override
    public HttpSession getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return income.getRemoteSocketAddress().toString();
    }

    @Override
    public String getRemoteHost() {
        //todo resolve hostname
        return income.getRemoteSocketAddress().toString();
    }

    @Override
    public void setAttribute(String name, Object o) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    public void refreshSession() {
       session.setLastAccessTime(LocalDateTime.now());
    }
}
