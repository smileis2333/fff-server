package org.example.fff.server.util;

import org.example.fff.server.servlet.Session;

import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {
    private ConcurrentHashMap<String, Session> sessions;

    public Session getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public void evictSession(String sessionId) {
        Session wtr = sessions.remove(sessionId);
        // todo，设置时间
    }
}
