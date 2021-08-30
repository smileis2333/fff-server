package org.example.fff.server.servlet;

import org.example.fff.server.util.SessionRegistry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session implements HttpSession {
    private String id;
    private SessionRegistry sessionRegistry;
    private Map<String, Object> attributes = new ConcurrentHashMap<>();
    private LocalDateTime createTime;
    private LocalDateTime lastAccessTime;
    private int maxInactiveInterval;

    private Session(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
        this.id = UUID.randomUUID().toString();
        this.createTime = LocalDateTime.now();
        this.lastAccessTime = this.createTime;
        this.maxInactiveInterval = sessionRegistry.getMaxInactiveInterval();
    }

    public void setLastAccessTime(LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public static Session newSession(SessionRegistry sessionRegistry) {
        Session session = new Session(sessionRegistry);
        sessionRegistry.addSession(session);
        return session;
    }

    @Override
    public long getCreationTime() {
        return createTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Object getValue(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return attributes.keySet().toArray(new String[]{});
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public void putValue(String name, Object value) {

    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public void removeValue(String name) {

    }

    @Override
    public void invalidate() {
        sessionRegistry.evictSession(this.getId());
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
